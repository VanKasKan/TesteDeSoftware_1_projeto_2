PS C:\Users\Gustavo\Documents\teste de software\Trabalho 2 de Teste Unidade 2\TesteDeSoftware_1_projeto_2> mvn -Parestas test
[INFO] Scanning for projects...
[INFO] 
[INFO] --------< br.com.aluguelcarros:aluguel-carros-teste-estrutural >--------
[INFO] Building aluguel-carros-teste-estrutural 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- resources:3.4.0:resources (default-resources) @ aluguel-carros-teste-estrutural ---
[INFO] skip non existing resourceDirectory C:\Users\Gustavo\Documents\teste de software\Trabalho 2 de Teste Unidade 2\TesteDeSoftware_1_projeto_2\src\main\resources
[INFO] 
[INFO] --- compiler:3.15.0:compile (default-compile) @ aluguel-carros-teste-estrutural ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- resources:3.4.0:testResources (default-testResources) @ aluguel-carros-teste-estrutural ---
[INFO] skip non existing resourceDirectory C:\Users\Gustavo\Documents\teste de software\Trabalho 2 de Teste Unidade 2\TesteDeSoftware_1_projeto_2\src\test\resources
[INFO] 
[INFO] --- compiler:3.15.0:testCompile (default-testCompile) @ aluguel-carros-teste-estrutural ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- surefire:3.5.2:test (default-test) @ aluguel-carros-teste-estrutural ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running br.com.aluguelcarros.CoberturaArestasTest
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.130 s -- in br.com.aluguelcarros.CoberturaArestasTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.970 s
[INFO] Finished at: 2026-11-06T17:27:31-03:00
[INFO] ------------------------------------------------------------------------
PS C:\Users\Gustavo\Documents\teste de software\Trabalho 2 de Teste Unidade 2\TesteDeSoftware_1_projeto_2> mvn -Pcaminhos test
[INFO] Scanning for projects...
[INFO] 
[INFO] --------< br.com.aluguelcarros:aluguel-carros-teste-estrutural >--------
[INFO] Building aluguel-carros-teste-estrutural 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- resources:3.4.0:resources (default-resources) @ aluguel-carros-teste-estrutural ---
[INFO] skip non existing resourceDirectory C:\Users\Gustavo\Documents\teste de software\Trabalho 2 de Teste Unidade 2\TesteDeSoftware_1_projeto_2\src\main\resources
[INFO] 
[INFO] --- compiler:3.15.0:compile (default-compile) @ aluguel-carros-teste-estrutural ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- resources:3.4.0:testResources (default-testResources) @ aluguel-carros-teste-estrutural ---
[INFO] skip non existing resourceDirectory C:\Users\Gustavo\Documents\teste de software\Trabalho 2 de Teste Unidade 2\TesteDeSoftware_1_projeto_2\src\test\resources
[INFO] 
[INFO] --- compiler:3.15.0:testCompile (default-testCompile) @ aluguel-carros-teste-estrutural ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- surefire:3.5.2:test (default-test) @ aluguel-carros-teste-estrutural ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running br.com.aluguelcarros.CaminhosBasicosTest
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.098 s -- in br.com.aluguelcarros.CaminhosBasicosTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.873 s
[INFO] Finished at: 2026-11-06T17:27:43-03:00
[INFO] ------------------------------------------------------------------------
PS C:\Users\Gustavo\Documents\teste de software\Trabalho 2 de Teste Unidade 2\TesteDeSoftware_1_projeto_2> mvn -Pmcdc test
[INFO] Scanning for projects...
[INFO] 
[INFO] --------< br.com.aluguelcarros:aluguel-carros-teste-estrutural >--------
[INFO] Building aluguel-carros-teste-estrutural 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- resources:3.4.0:resources (default-resources) @ aluguel-carros-teste-estrutural ---
[INFO] skip non existing resourceDirectory C:\Users\Gustavo\Documents\teste de software\Trabalho 2 de Teste Unidade 2\TesteDeSoftware_1_projeto_2\src\main\resources
[INFO] 
[INFO] --- compiler:3.15.0:compile (default-compile) @ aluguel-carros-teste-estrutural ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- resources:3.4.0:testResources (default-testResources) @ aluguel-carros-teste-estrutural ---
[INFO] skip non existing resourceDirectory C:\Users\Gustavo\Documents\teste de software\Trabalho 2 de Teste Unidade 2\TesteDeSoftware_1_projeto_2\src\test\resources
[INFO] 
[INFO] --- compiler:3.15.0:testCompile (default-testCompile) @ aluguel-carros-teste-estrutural ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- surefire:3.5.2:test (default-test) @ aluguel-carros-teste-estrutural ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running br.com.aluguelcarros.McdcTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.119 s -- in br.com.aluguelcarros.McdcTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.940 s
[INFO] Finished at: 2026-11-06T17:27:52-03:00
[INFO] ------------------------------------------------------------------------