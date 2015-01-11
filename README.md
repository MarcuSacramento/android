# android
Repositório para Projetos Android
Preparando Ambiente para o GitHub
1- Instalar Git Core
	sudo apt-get install git-core
2- Criar chave para acesso SSH
	ssh-keygen -t rsa -C "comment"
3- Instalar XClip para copiar a chave criada
	sudo apt-get install xclip
4- Copiar a chave SSH para o GitHub
	cat ~/.ssh/id_rsa.pub | xclip -sel clip
5- Configurar usuário
	git config --global user.name "Your Name"
	git config --global user.email codexico@gmail.com
6- Abrir sessão no GitHub
	ssh git@github.com

Criar Projeto no GitHub
1- Criar espaço para o Projeto
	mkdir nomedoprojeto
	cd nomedodiretorio
2- Iniciar o git nesse diretório(Criará um Diretório .git)
	git init
3- Adicionar Repositório ao GitHub(caso não exista)
	git remote add «projeto» git@github.com:«username»/«Diretório do projeto».git
4- Baixar o Projeto
	git pull «projeto» «Nome do Branch»

Usando o GitHub«»
#- Criar arquivo(Local)
	touch «nomedoarquivo»
#- Adicionar Alteração de um arquivo(Local)
	git add «nomedoarquivo»
#- Adicionar todas as alterações(Local)
	git add .
#- Commit das alterações(Versionamento Local)
	git commit -m "«Mensagem para o versionador»"
#- git push «projeto» «Nome do Branch»
