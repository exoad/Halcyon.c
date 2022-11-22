# OSX DISTRIBUTION

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
        if [[ "$1" == "x86" ]] ; then
            gcc -c -m32 -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin "./pkg/halcyonstl/$i" -o "./hlib/out/$(basename $i | cut -f1 -d".")_x86.o"
            gcc -dynamiclib -m32 -fPIC -o ./hlib/$(basename $i | cut -f1 -d".")_x86.dylib "./hlib/out/$(basename $i | cut -f1 -d".")_x86.o" -lc
        else
            gcc -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin "./pkg/halcyonstl/$i" -o "./hlib/out/$(basename $i | cut -f1 -d".")_x64.o"
            gcc -dynamiclib -fPIC -o ./hlib/$(basename $i | cut -f1 -d".")_x64.dylib "./hlib/out/$(basename $i | cut -f1 -d".")_x64.o" -lc
        fi
    fi
done