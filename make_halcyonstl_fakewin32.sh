# LINUX DISTRIBUTIONS

if [ -d "./hlib" ] ; then
    echo "refreshing the hlib folder"
    cd ./hlib
    rm -rf *.so *.o
    cd ../
else
    mkdir hlib
    echo "created hlib folder. reason: does not exist"
fi

if [ -d "./hlib/out" ] ; then
    echo "refreshing the hlib output folder"
    cd ./hlib/out
    rm -rf *.so *.o
    cd ../../
else
    mkdir hlib/out
    echo "created hlib output folder. reason: does not exist"
fi

echo "starting compilation & linking of native stuffs"
for i in $(ls pkg/halcyonstl) ; do
    if [[ $i =~ .*\.cc$ ]] ; then # if we fine the required cc file which shld provide implementation
        echo "compiling: $i"
        g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/win32 "./pkg/halcyonstl/$i" -o "./hlib/out/$(basename $i | cut -f1 -d".").o"
        echo "linking: $i"
        g++ -shared -fPIC -o hlib/$(basename $i | cut -f1 -d".").dll "./hlib/out/$(basename $i | cut -f1 -d".").o" -Wl,-add-stdcall-alias
    fi
done