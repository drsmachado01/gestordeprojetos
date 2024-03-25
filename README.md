# gestordeprojetos
Gestor de projetos

Para executar:
 mvn spring-boot:run
 
ATENÇÃO
É preciso alterar o parâmetro

spring.profiles.active=test

Para

spring.profiles.active=prod

Para que a execução possa ocorrer, caso contrário, a estrutura de testes será montada.

Detalhes importantes:

- A inclusão de membros se dá pelo controller MembroController, através do path: /projetos/{idProjeto}/membros
Passando como parâmetro um úncio funcionario, contendo nome e a atribuição