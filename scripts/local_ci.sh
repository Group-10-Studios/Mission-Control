#!/bin/bash
## This script is designed to facilitate running the same tasks as our CI pipeline on your local machine
## This is designed for use with a bash machine with a few requirements including:
## wget, node.JS, java11 and maven
##
## As long as these are installed this should run on the machine and replicate the results you could recieve
## in our CI pipeline implementation
## Author: Nathan Duckett

# Function to get parameters from cli arguments
function get_params () {
    # =====================================
    # Parameter parsing for bash script
    # =====================================
    PARAMS=""
    while (( "$#" )); do
    case "$1" in
        -v|--verbose)
        verbose="true"
        shift
        ;;
        -h|--headless)
        headless="true"
        shift
        ;;
		-vh|--verbose-headless)
        headless="true"
		verbose="true"
        shift
        ;;
        --) # end argument parsing
        shift
        break
        ;;
        -*|--*=) # unsupported flags
        echo "Error: Unsupported flag $1" >&2
        exit 1
        ;;
        *) # preserve positional arguments
        PARAMS="$PARAMS $1"
        shift
        ;;
    esac
    done
    # set positional arguments in their proper place
    eval set -- "$PARAMS"
}

function echoRed () {
	echo -e "\e[31m$1\e[0m"
}

function verify_docs () {
	if [[ "$verbose" == "true" ]]; then
		mvn javadoc:javadoc
	else
		javadocResult=$(mvn javadoc:javadoc 2>&1)
	fi
	if [[ $? != 0 ]]; then
		echoRed "There was an error with your javadoc comments - please run: 'mvn javadoc:javadoc' to view the output"
		exit 1
	fi
	# Check if NPM is installed to run spellcheck
	if [[ -x "$(command -v npm)" ]]; then
		# This assumes you have nodeJS installed
		npm install markdown-spellcheck 2>1 > /dev/null
		if [[ "$verbose" == "true" ]]; then
			./node_modules/markdown-spellcheck/bin/mdspell -r -n -a '**/*.md' '!**/node_modules/**/*.md'
		else
			result=$(./node_modules/markdown-spellcheck/bin/mdspell -r -n -a '**/*.md' '!**/node_modules/**/*.md')
		fi
		if [[ $? != 0 ]]; then
			[ "$verbose" != "true" ] && echoRed "There were spelling mistakes with your document - please rerun this with '-v' to view those mistakes or with './node_modules/markdown-spellcheck/bin/mdspell -r -n -a '**/*.md' '!**/node_modules/**/*.md''" || echoRed "Please read the spelling mistakes above."
			exit 1
		fi
	else
		echoRed "NPM is not available - not running spellcheck"
	fi
}

function find_bugs () {
	if [[ "$verbose" == "true" ]]; then
		mvn clean compile spotbugs:check
	else
		result=$(mvn clean compile spotbugs:check 2>&1)
	fi
	if [[ $? != 0 ]]; then
		echoRed "Find Bugs produced errors please run: 'mvn clean compile spotbugs:check' to view the output"
		exit 1
	fi
}

function checkstyle () {
	if [[ ! -f "checkstyle-8.32-all.jar" ]]; then
		if [[ -x "$(command -v wget)" ]]; then
			wget -q https://github.com/checkstyle/checkstyle/releases/download/checkstyle-8.32/checkstyle-8.32-all.jar
		else
			echoRed "WGET is not available - unable to download or run checkstyle"
			return
		fi
	fi
	result=$(java -jar checkstyle-8.32-all.jar -c checkstyle.xml src/)
	expected_result="Starting audit...Audit done."
	test_res=${result//$'\n'/}
      	if [ "$test_res" != "$expected_result" ]; then
        	>&2 echo "Checkstyle has Failed"
        	echo "$result" | while IFS= read -r line; do
			 echo "$line"
		done
		$(exit 1)
	fi
}

function run_tests () {
	if [[ "$headless" == "true" ]]; then
		exclude="-DexcludePackage=nz.ac.vuw.engr300.gui.**"
	fi
	# Output if verbose
	if [[ "$verbose" == "true" ]]; then
		mvn $exclude clean test
	else
		result=$(mvn $exclude clean test 2>&1)
	fi
	# Check return code is 0
	if [[ $? != 0 ]]; then
		echoRed "Test suite has failed - please run: 'mvn clean test' to view the output"
		exit 1
	fi
}


function run_local_CI () {
	echo "> Verifying documentation"
	verify_docs
	echo "> Finding bugs"
	find_bugs
	echo "> Checking Checkstyle"
	checkstyle
	echo "> Running test suite"
	run_tests
	# Printing in Green text
	echo -e "\e[32mPipeline is passing as expected\e[0m"
}

get_params $@
run_local_CI
