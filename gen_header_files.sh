# LINUX, MACOSX, UNIX DISTRIBUTIONS ONLY

#check if the valid folders are there cuz they might not be there????

if [ ! -d "./pkg/halcyonstl" ] ; then
    echo "making the impl folder"
    mkdir pkg/halcyonstl
fi

if [ ! -d "./pkg/halcyonstl/include" ] ; then
    echo "making the include folder"
    mkdir pkg/halcyonstl/include
fi

echo "starting generation process"
for i in $(find pkg -name "*.java") ; do
  echo "generating... $1"
  javac -nowarn -h "pkg/halcyonstl/include" $i
done
echo "done"

find -name "*.class" -exec rm -f {} \;