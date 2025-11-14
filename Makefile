SRC_DIRS := main tests
BIN_DIR := bin
JAR := ojdbc6.jar

SRC_FILES := $(foreach dir,$(SRC_DIRS),$(wildcard $(dir)/*.java))

all: $(BIN_DIR) classes

$(BIN_DIR):
	mkdir -p $(BIN_DIR)

classes: $(BIN_DIR)
	javac -d $(BIN_DIR) -cp $(JAR) $(SRC_FILES)

run: all
	java -cp $(BIN_DIR):$(JAR) tests.main_query

test: run

clean:
	rm -rf $(BIN_DIR)

