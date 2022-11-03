# should only be used on UNIX, LINUX, MACOSX systems!!! win32 and dos related systems will not work
# remove all *.class files do:
# find -name "*.class" -exec rm -f {} \;

#argument(s) required:
# 1 -> platform: linux, osx
# 2 -> execution: y, n
# 3 -> arch: x86, x64
echo "Running from $(pwd)"
if [ ! $# -eq 0 ] ; then
    if [ "$1" == "linux" ] ; then
        bash make_halcyonstl_linux.sh "$3"
    elif [ "$1" == "osx" ] ; then
        bash make_halcyonstl_osx.sh "$3"
    else
        bash make_halcyonstl_fakewin32.sh "$3"
    fi

    if [ ! -d "h_out" ] ; then
        mkdir h_out
    else
        rm -rf h_out
        mkdir h_out
    fi
    VAR_JARS=""
    for i in $(ls ./lib) ; do
        if [[ $i =~ .*\.jar$ ]] ; then
            VAR_JARS+="lib/$i:"
        fi
    done
    if [ "$2" == "y" ] ; then
        VAR_JARS=${VAR_JARS::len-1}
        CURR_PATH=$(pwd)
        echo $VAR_JARS
        find -name "*.java" > sources.gen
        ${JAVA_HOME}/bin/javac -d \"$CURR_PATH"h_out/"\" --source-path "@sources.gen" -cp "$VAR_JARS"
        ${JAVA_HOME}/bin/java -Xdiag -XX:+ShowCodeDetailsInExceptionMessages -Djava.library.path="hlib" -cp "$CURR_PATH/h_out:$VAR_JARS" com.jackmeng.Halcyon
    fi
fi