"""Integrações de qualidade. Requer Python 3, Git, Maven e Java no PATH."""
import csv
import json
import shutil
import subprocess
import sys
from pathlib import Path
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
COMMIT = 'e89ce1a28d56db314d159e8957591aef6002173d'
ADAPTER = 'junit5-magic-zero-v1'


def run(*args, cwd=ROOT):
    subprocess.run([str(arg) for arg in args], cwd=cwd, check=True)


def tsdetect():
    output = ROOT / 'target/tsdetect'
    source = ROOT / 'target/tools/tsdetect'
    jar = source / 'target/TestSmellDetector-0.1-jar-with-dependencies.jar'
    output.mkdir(parents=True, exist_ok=True)
    marker = source / 'target/adapter-version.txt'
    if not jar.exists() or not marker.exists() or marker.read_text() != ADAPTER:
        if not source.exists():
            source.parent.mkdir(parents=True, exist_ok=True)
            run('git', 'clone', 'https://github.com/TestSmells/TestSmellDetector.git', source)
        run('git', 'checkout', '--detach', COMMIT, cwd=source)
        # Compatibilidade JUnit 5: não muda os detectores ou seus limiares.
        util = source / 'src/main/java/testsmell/Util.java'
        text = subprocess.check_output(['git', 'show', COMMIT + ':src/main/java/testsmell/Util.java'], cwd=source, text=True)
        start = text.index('    public static boolean isValidTestMethod')
        end = text.index('    public static boolean isInt', start)
        text = text[:start] + '''    public static boolean isValidTestMethod(MethodDeclaration n) {
        return n.getAnnotationByName("Test").isPresent()
            || n.getAnnotationByName("ParameterizedTest").isPresent()
            || n.getAnnotationByName("RepeatedTest").isPresent();
    }

    public static boolean isValidSetupMethod(MethodDeclaration n) {
        return n.getAnnotationByName("Before").isPresent()
            || n.getAnnotationByName("BeforeEach").isPresent()
            || n.getNameAsString().equals("setUp");
    }

''' + text[end:]
        util.write_text(text)
        detector = source / 'src/main/java/testsmell/TestSmellDetector.java'
        text = subprocess.check_output(['git', 'show', COMMIT + ':src/main/java/testsmell/TestSmellDetector.java'], cwd=source, text=True).replace(
            'typeDeclaration.getMethods().size()',
            '(int) typeDeclaration.getMethods().stream().filter(Util::isValidTestMethod).count()')
        text = text.replace('TypeDeclaration typeDeclaration', 'TypeDeclaration<?> typeDeclaration')
        detector.write_text(text)
        # Correção upstream: zero ocorrências não pode satisfazer limiar zero.
        magic = source / 'src/main/java/testsmell/smell/MagicNumberTest.java'
        text = subprocess.check_output(['git', 'show', COMMIT + ':src/main/java/testsmell/smell/MagicNumberTest.java'], cwd=source, text=True)
        magic.write_text(text.replace('magicCount >= thresholds.getMagicNumberTest()', 'magicCount > thresholds.getMagicNumberTest()'))
        run(shutil.which('mvn') or 'mvn', '-B', '-DskipTests', 'package', cwd=source)
        marker.write_text(ADAPTER)
    tests = sorted((ROOT / 'src/test/java').rglob('*.java'))
    if not tests:
        raise RuntimeError('Nenhum arquivo de teste encontrado.')
    mapping = json.loads((ROOT / 'scripts/tsdetect-mapping.json').read_text())
    rows = []
    for test in tests:
        relative = test.relative_to(ROOT / 'src/test/java')
        production_relative = mapping.get(relative.as_posix(), relative.with_name(relative.name.replace('Test.java', '.java')).as_posix())
        production = ROOT / 'src/main/java' / production_relative
        if not production.is_file():
            raise RuntimeError(f'Sem classe de produção correspondente: {test}')
        rows.append(['aluguel-carros', str(test), str(production)])
    with (output / 'input.csv').open('w', newline='') as stream:
        csv.writer(stream).writerows(rows)
    report = output / 'test-smells.csv'
    report.unlink(missing_ok=True)
    run('java', '-jar', jar, '-f', output / 'input.csv', '-t', 'default', '-g', 'numerical', '-o', report)
    with report.open(newline='') as stream:
        reader = csv.DictReader(stream)
        findings = list(reader)
        smells = reader.fieldnames[7:]
    if len(findings) != len(tests) or len(smells) != 21:
        raise RuntimeError('Relatório incompleto: esperados todos os arquivos e 21 detectores.')
    analyzed = {Path(row['TestFilePath']).resolve() for row in findings}
    if analyzed != {test.resolve() for test in tests}:
        raise RuntimeError('A lista de arquivos analisados difere da suíte.')
    if any(int(row['NumberOfMethods']) <= 0 for row in findings):
        raise RuntimeError('Arquivo sem testes reconhecidos pelo tsDetect.')
    # Surefire registra os nomes dos métodos (várias invocações contam uma vez).
    executed = set()
    ignored = 0
    for xml in (ROOT / 'target/surefire-reports/suite').glob('TEST-*.xml'):
        for case in ET.parse(xml).iter('testcase'):
            if case.find('skipped') is not None:
                ignored += 1
            executed.add((case.attrib['classname'], case.attrib['name'].split('(')[0].split('[')[0]))
    detected_by_class = {
        '.'.join(Path(row['TestFilePath']).resolve().relative_to(ROOT / 'src/test/java').with_suffix('').parts): int(row['NumberOfMethods'])
        for row in findings
    }
    executed_by_class = {}
    for class_name, method_name in executed:
        executed_by_class[class_name] = executed_by_class.get(class_name, 0) + 1
    if detected_by_class != executed_by_class:
        raise RuntimeError(f'Abrangência divergente por classe: tsDetect={detected_by_class}, Surefire={executed_by_class}. Execute a suíte completa.')
    detected_count = sum(int(row['NumberOfMethods']) for row in findings)
    if not executed or detected_count != len(executed):
        raise RuntimeError(f'Abrangência divergente: tsDetect={detected_count}, métodos Surefire={len(executed)}. Verifique recursos JUnit não suportados.')
    totals = {smell: sum(int(row[smell]) for row in findings) for smell in smells}
    summary = {'commit': COMMIT, 'adaptacao': ADAPTER,
               'arquivos': len(tests), 'metodos': detected_count, 'metodos_por_classe': detected_by_class, 'ocorrencias': totals}
    (output / 'summary.json').write_text(json.dumps(summary, indent=2, ensure_ascii=False) + '\n')
    print(json.dumps(summary, indent=2, ensure_ascii=False))
    if ignored:
        raise RuntimeError('Há testes ignorados pelo JUnit; a suíte completa deve ser analisada e executada.')
    if any(totals.values()):
        raise RuntimeError('tsDetect encontrou test smells. Consulte target/tsdetect/.')


def pit():
    report = ROOT / 'target/pit-reports/mutations.xml'
    mutations = list(ET.parse(report).getroot())
    if not mutations:
        raise RuntimeError('PIT não gerou mutantes.')
    statuses = {}
    for mutation in mutations:
        status = mutation.attrib['status']
        statuses[status] = statuses.get(status, 0) + 1
    (report.parent / 'status-summary.json').write_text(json.dumps(statuses, indent=2) + '\n')
    print(statuses)
    if any(status != 'KILLED' for status in statuses):
        raise RuntimeError('Há mutantes sem status KILLED. Consulte mutations.xml e justifique em resposta.md.')


if __name__ == '__main__':
    try:
        {'tsdetect': tsdetect, 'pit': pit}[sys.argv[1]]()
    except (RuntimeError, subprocess.CalledProcessError) as error:
        sys.exit(str(error))
