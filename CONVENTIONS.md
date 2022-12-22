# Documentation | 文

This markdown document provides a simple overview of how things go around regarding usage & readability of source code (not this repo!).

## File Naming

File Naming, source suffixing, represents how most Java Source Files are named based on their intended purposes / category.

### JAVA Source Files

**Formatting**
Most if not all Java Source Files provided do not align directly with the standard Java Coding Standards/Conventions. Instead archaic formatting from other common languages are borrowed:

1. Indentation Style "[Allman](https://en.wikipedia.org/wiki/Indentation_style#Allman_style)":
   Using the Allman indentation style with opening `{` braces on a new line.

```java
public class Test_IO
{
  public static native void print(String... e);
}
```

2. Type Attributes `extends`,`implements` -> `class`,`interface`,`abstract class`
   Attributes are presented in a vertical list like manner for easy readability.<br>

```java
public class HelloMe
    extends
    World
    implements
    Runnable,
    Comparable<Integer>,
    Serializable
{

}
```

3. [Snake Case](https://en.wikipedia.org/wiki/Snake_case), [Pascal Case](https://wiki.c2.com/?PascalCase), [Camel Case](https://en.wikipedia.org/wiki/Camel_case)
   Most Java source files use method names with snake casing with some camel casing for methods are to be used for outside of the standard Halcyon environment.

**File Name Prefixing**

| <strong>Prefix</strong> |                                                                                                                            <strong>Description</strong>                                                                                                                             |
| :---------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
|   <code>gui\_</code>    |                                                          Represents standalone GUI Components that depend on <code>dgui\_</code> components to make a whole usable component. Also known as the boilerplate GUI Component                                                           |
|   <code>dgui\_</code>   |                                                                             Represents dependent GUI Components that require itself to be added to a <code>gui\_</code> based component for it to work                                                                              |
|   <code>use\_</code>    | Defines general functionality; functionalities could either be all <code>static</code> or a usuable class defining an instance for functionality. Generally known as a usable. Note: If all usability functions are declared as static, this usable known as a "boilerplate usable" |
|  <code>const\_</code>   |                                                    Defines constants, most often encountered as an <code>interface</code>. Should not define a constructor & functions (unless for additionally functionality to the constants)                                                     |
|   <code>sys\_</code>    |                                                                 A </code>use\_</code> variation that clearly specifies that this class defines and/or uses functionality that are defined with <code>native</code>                                                                  |
|    <code>NONE</code>    |                   When no prefix is encountered, it should be noted as a "Runner" class meaning it most likely defines a <code>public static void main</code> OR it could be a <code>use\_</code> (or variations of it) that is used commonly: saves time typing                    |
|   <code>impl\_</code>   |                                                              Defines a abstraction of <code>abstract class</code> OR <code>abstract interface</code> where certain functionality is abstracted and to be implemented.                                                               |
|   <code>evnt\_</code>   |                                                                      A <code>impl\_</code> variation that clearly specifies that this abstraction is used for event handling, specifically for event callbacks                                                                      |

**For child classes** like the class `use_Struct.java`, which defines usable data structures, all child classes start with the class name (excluding prefix & underscore), so an existing child class would be `struct_Pair` or `struct_Trio` under `use_Struct`.

### SCRIPT Files

| <strong>Prefix</strong> |                                                                                                <strong>Description</strong>                                                                                                 |
| :---------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
|   <code>run\_</code>    | Represents the entirety of most RUNNABLE script files, like BASH, POWERSHELL, etc.. Runnable meaning it is intended to be executed via a Shell by the user. Runnables are usually meant to be source files and not compiled |

### Locale Resource Files

|  <strong>Prefix</strong>   |                                      <strong>Description</strong>                                       |
| :------------------------: | :-----------------------------------------------------------------------------------------------------: |
| <code>HalcyonLang\_</code> | A Halcyon Language Locale file. This prefix is most likely followed by the language code (e.g. "en_US") |
