[33mcommit 39fe56d1e1f4eff9867c98f667efc739ffdebb0b[m[33m ([m[1;36mHEAD -> [m[1;32mmaster[m[33m, [m[1;31morigin/master[m[33m)[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Thu Mar 9 15:58:08 2023 -0300

    Atualizado validações ao ler, para ajudar no tratamento de bugs caso o arquivo CSV esteja corrompido

[33mcommit 8451d871e7a2d0408c703ad7054b40164584a7f7[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Thu Mar 9 15:36:56 2023 -0300

    Implementando validações e logs ao ler um arquivo CSV. Por exemplo, se tiver algum dado corrompido vai retornar pro front o erro e em que linha aconteceu, e também vai aparecer o log

[33mcommit 3c6e2f2a29fd0e703ff8e7302b47cf7a00aae941[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Wed Mar 8 16:24:30 2023 -0300

    Transferida a lógica de paginação para a service, porém, o getV2 ficou estranhão. Necessário tratar melhor o CSV, se vc zoa o arquivo e pede pra ler ele buga. Mas o que acontece é que a forma que eu programei pra consumir a API mockada e salvar em CSV, se o usuario fizer duas request vão ter 2 cabeçalhos Id Nome etc.

[33mcommit 9aed5f977acb4e3982d62fbc937c30fff7eee5b6[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Wed Mar 8 13:50:02 2023 -0300

    Corrigido bug no filtro. O filtro era aplicado de forma incorreta, como se fosse um parametro ou o outro, e não de forma exclusiva: nasceu dia 12, tem sexo F e tem no mínimo 25 anos

[33mcommit 50c1b7cf2e6534e32f40f98869512deda05eba68[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Wed Mar 8 12:37:03 2023 -0300

    Passando as classes para inglês

[33mcommit edefeac6fe61ddcce8857c3d50c3ba444fbaa384[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Wed Mar 8 11:30:56 2023 -0300

    Arrumado erro de arquitetura. Hierarquia Controller > Service respeitada.

[33mcommit e22c76dd1595b8ff1465aee0ff058206f8d0c26e[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Tue Mar 7 16:11:03 2023 -0300

    Corrigido bug ao tentar criar o arquivo csv. Todo o processo é um flux, porém eu retornava um Objeto Response que não fazia parte do flux. Ou seja, a operação finalizava e retornava o status antes de realmente gravar.

[33mcommit f3929d198bc7d747ddd2651d9a87b02581e9a47c[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Tue Mar 7 15:32:05 2023 -0300

    Tentando melhorar a hierarquia, fazendo a controller apenas conversar com a service

[33mcommit 88289bc6f696173c979eec2bb84720f31f605f28[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Tue Mar 7 14:37:11 2023 -0300

    Removido exemplos do swagger que só enchiam o saco no quesito teste

[33mcommit 4404a4b320002b7041f3d8114311a08b12d74f8e[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Tue Mar 7 14:14:44 2023 -0300

    Corrigido bugs serveros sobre o filtro. Eu utilizava na lógica o método .toList() do Page, porém, se você não iterar sobre o objeto Page, ele retonará apenas uma página. Acabei por implementar o uso de DTO para fazer a lista percorrer e apenas paginar na classe responsável por filtrar.

[33mcommit db1459ade4bb59439d69f4bda9de1872a06f21b0[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Tue Mar 7 14:13:40 2023 -0300

    Corrigido bugs serveros sobre o filtro. Eu utilizava na lógica o método .toList() do Page, porém, se você não iterar sobre o objeto Page, ele retonará apenas uma página. Acabei por implementar o uso de DTO para fazer a lista percorrer e apenas paginar na classe responsável por filtrar.

[33mcommit 864b2730d2884c6bfcae40e2d2d6888b7dcad58c[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Tue Mar 7 12:23:56 2023 -0300

    Introduzido jakarta (ainda sem mensagens customizadas) e corrigido bug em que não tinha definido inicio e fim do regex para validação da data de nascimento, ou seja, aceitaria qualquer coisa contato que tenha dd-mm-yyyy

[33mcommit c5828581dc848b47b7d763cdb62b2423501577b6[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Tue Mar 7 11:09:13 2023 -0300

    criada uma nova classe de exemplo: CustomPage. Apenas fins didáticos já que a lógica já foi feita usando CustomPageImpl

[33mcommit 1bf48fa957577edf72429d3cc7de56a512162df7[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 18:08:05 2023 -0300

    bug corrigido

[33mcommit dc5f060ea26e6d36f16d61004591d60445735348[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 18:00:32 2023 -0300

    corrigido bug

[33mcommit f35c0e77c380b02ff51628d02c7f9e8b71c7039a[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 17:58:59 2023 -0300

    Melhorada lógica do POST e PUT, porém ainda é necessário a implementação do model mapper ao invés da conversão manual

[33mcommit 8a8a4fd0bc65c526be42defe81ecbe4edf667136[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 17:22:51 2023 -0300

    Deletado códigos redundantes

[33mcommit 490c35048ca276b2a0f30022becdbfc39219ff72[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 17:08:51 2023 -0300

    Melhorei a interface do swagger

[33mcommit 75d0f5c61c376ee465f34f9e158482bf91b6c3fe[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 16:33:25 2023 -0300

    Parte da documentação realizada

[33mcommit 69028776302a7ed0107a4ff58007dc3fad67405c[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 14:16:39 2023 -0300

    Exceções tratadas caso o usuário da API digite uma data de nascimento inválida.

[33mcommit 2fad0b1e95e3c69cbe69773621ec87c7df66fc9d[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 12:34:47 2023 -0300

    Começando a documentar com Swagger

[33mcommit 1ff887fb04dd32a10e36233096d380fc8e1ab1fb[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 08:59:57 2023 -0300

    Separada a responsabilidade de acessar os dados para classe DAO

[33mcommit df0d0797b92d6ff7b00e2ceb2386dcc69b271cf6[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 06:10:02 2023 -0300

    Bug corrigido no delete. Agora ao criar o JSON retornado vem com o ID gravado no arquivo csv

[33mcommit 9ed731c82fa2a87ae11e54b8ae07716840d7d637[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 04:51:20 2023 -0300

    Corrigido bug null pointer exception do deletar.

[33mcommit 16ceff93d61ee7388d676a068815a35bdb0d0630[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 04:32:08 2023 -0300

    Começando a tratar exceções

[33mcommit abdd00691544dec5b1e60444cb3d44b31a70fb2b[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 03:58:42 2023 -0300

    Todos endpoints atualizados para devolver um Wrapper contendo Clientes e MetaData, metadata este que possui outras informações como qtdClientes

[33mcommit e60c6ef66c0fddbe4f44625e0086f30c27974a6e[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 02:56:41 2023 -0300

    Excluida classe inutilizada

[33mcommit e7554366cb3d012825522234440be418218defb4[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Mar 6 02:52:34 2023 -0300

    Corrigido bugs, criada paginas custom de paginação para controlar as informações, retornando agora um ClienteWrapper que contém meta data.

[33mcommit 166623983901ef7d53faa42b49f6ca01212ae92e[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Fri Mar 3 14:18:28 2023 -0300

    Endpoint para deletar linhas do arquivo CSV adicionado. Ainda é necessário refatorar algumas partes para ter menos código repetido e maior legibilidade. Após isso, documentação com Swagger.

[33mcommit d492b3f7c5184af5a01b3c427e06b6703c562d26[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Fri Mar 3 11:51:00 2023 -0300

    Filtro melhorado. Mais opções e corrigido bug.

[33mcommit e43084c090bfbd29c98dd775e43c0a381e901a77[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Thu Mar 2 13:27:45 2023 -0300

    filtro melhorado

[33mcommit 3ffb32eb39719fb5f98f64fc61ec517def06908c[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Thu Mar 2 09:20:30 2023 -0300

    Endpoints criados, faltando apenas o Delete. Ainda há necessidade de refatorar o código para deixar mais legível e também falta a implementação de autenticação por JWT.

[33mcommit 0d7828f4c98cd0b08082da06142cfcd28fa85c66[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Thu Mar 2 06:55:30 2023 -0300

    Implementado o POST e GET do .csv

[33mcommit 2adb90f3a11226b036a27f060345c1167d690a49[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Thu Mar 2 06:01:03 2023 -0300

    Implementado o POST e GET do .csv

[33mcommit 8a6469bfd94a2de8fb0ab973728eb7e2fcbeb906[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Thu Mar 2 01:21:46 2023 -0300

    agora o arquivo CSV separa cada campo por ; ao invés de , porém, a implementação da lógica do header do csv não é das melhores. Necessário refatoramento dessa parte.

[33mcommit b947e3f83eef74c78db166c504f2f2f7bc2989cf[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Wed Mar 1 17:53:33 2023 -0300

    O básico do CSV foi implementado. Próximo passo refatorar o código para algo mais legível e arrumar o padrão do CSV, esta retornando com virgulas enquanto o combinado foi ponto e virgula (;)

[33mcommit e292157fba4f7314b17874f83afd537e290c93d6[m
Merge: dcf565a b33c9d4
Author: Renato <renaato.d.m@gmail.com>
Date:   Wed Mar 1 05:20:39 2023 -0300

    Merge branch 'master' of https://github.com/RenatoDaM/api-challenge

[33mcommit dcf565a26735d3e94a2372c45a7b14bdb4f32a49[m[33m ([m[1;31morigin/feature/assincrono[m[33m, [m[1;32mfeature/assincrono[m[33m)[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Wed Mar 1 05:17:26 2023 -0300

    Começando a criar o endpoint para criar um arquivo CSV.

[33mcommit b33c9d42133eb23c0587e1e672136aba8de0b6e9[m
Merge: 3d9722e f775efb
Author: Renato Davoli <112330702+RenatoDaM@users.noreply.github.com>
Date:   Wed Mar 1 02:03:32 2023 -0300

    Merge pull request #1 from RenatoDaM/feature/assincrono
    
    Feature/assincrono

[33mcommit f775efb139508a540ebb707c2644af6f9e63adff[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Wed Mar 1 02:02:04 2023 -0300

    Corrigido bug da classe ClienteFilter. Ela não filtrava de acordo pelo sexo nem data de nascimento, pois as Strings apontavam pra endereços diferentes de memória, sendo necessário o método da classe String .equals() dada a natureza das Strings em Java.

[33mcommit a37dee565bf79c00c26a564f88d13e06b84811d5[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Tue Feb 28 17:20:33 2023 -0300

    convertendo todo o caminho da getClientesV2 em reativo

[33mcommit a98ffdf4b7033949fd5b86c3e15cb86d2bb97a57[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Tue Feb 28 15:06:22 2023 -0300

    Método versão assíncrona implementada.

[33mcommit 3d9722e9a53c7d1abc7d3618bc75c81993672ef2[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Tue Feb 28 13:34:12 2023 -0300

    Configurando com Beans

[33mcommit 6ad3853cf1dbcdd4620ce9b4b9f10ed851189b1f[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Feb 27 16:32:40 2023 -0300

    Corrigido bug aonde o conversor de aniversário para data de nascimento poderia subtrair um ano mais de uma vez

[33mcommit 74f4ef2c52bc4fd7ef91bf12f9cd0ee2082ce512[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Feb 27 05:44:36 2023 -0300

    Versionado e otimizado. Agora no V2 retorna sequenciado com ID, assim como o pedido do segundo desafio. Atualizado para uso de IntStream.

[33mcommit aba2dfc56569fd14283346ae92968f89711f1f3f[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Mon Feb 27 01:45:01 2023 -0300

    Troquei o mapeamento JSON para objeto. Antes era pelo JSON ORG, agora é tudo pela biblioteca jackson.

[33mcommit 0e3cffe82320401223b91e799f1f40014a632971[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Sun Feb 26 20:13:05 2023 -0300

    Filtro do cliente implementado

[33mcommit 31875bcaf247aee0b696ad9120a8a9d149543799[m
Author: Renato <renaato.d.m@gmail.com>
Date:   Sun Feb 26 06:17:20 2023 -0300

    Json tratado, começando a implementar a paginação e filtros. Endpoint GET funcional porém incompleto.
