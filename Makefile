MVN := mvn

.PHONY: help clean compile test verify package install dependency-tree run

help:
	@echo "Comandos disponíveis:"
	@echo "	make clean            Limpa os arquivos compilados"
	@echo "	make compile          Compila o projeto"
	@echo "	make test            Executa os testes"
	@echo "	make verify          Compila e executa todas as verificações"
	@echo "	make package         Gera o arquivo JAR"
	@echo "	make install         Instala o JAR no repositorio local"
	@echo "	make dependency-tree  Mostra as dependencias"

run: package
	@if [ "$(MODE)" = "console" ] || [ "$(MODE)" = "gui" ]; then \
		echo "Executando no modo $(MODE)..."; \
		java -jar target/swingy-1.0-SNAPSHOT.jar $(MODE); \
	else \
		echo "Uso: make run MODE=console"; \
		echo "  ou: make run MODE=gui"; \
		exit 1; \
	fi

clean:
	$(MVN) clean

compile:
	$(MVN) compile

test:
	$(MVN) clean test

verify:
	$(MVN) clean verify

package:
	$(MVN) clean package

install:
	$(MVN) clean install

dependency-tree:
	$(MVN) dependency:tree