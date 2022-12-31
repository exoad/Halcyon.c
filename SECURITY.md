# <strong>System Compatibility</strong>

A schema that details this software's compatibility with your system.

## Operating System Specific(s)

Things relating to your operating system.

### Required System Bitness

> All prebuilt distributables are distributed with only one bitness. If you want to use the software
> for a different bitness, you must compile it yourself.

**Required** X86-64

### Native Images

> Native images exist as a binary executable that could be easily executed by the OS. All of these
> are prebuilt distributables

**Supported:**

1. Linux (Any major flavors: Ubuntu, Fedora, etc..)
2. OSX (Leopard 10.5.8 and above)
3. Windows (7, 8, 10, 11)

**Unsupported:**

1. Solaris
2. Open/FreeBSD

### JAR-With precompiled native libraries

> A JAR file is compiled and is packaged together with the necessary native libraries. Purely reliant on
> the host machine being able to run a JRE

**Recommended-JRE-Version** 15

**Min-JRE-Version** 11

**Unsupported-JRE-Version(s)** 8 and below
<br>It is highly unrecommended to build a native image of the program with Java standard of 10 and below, as there are certain features that are not introduced and certain program optimizations that are not taken into consideration. For the best consumer experience, use the prebundled native images and lean away from building your own!

**Untested-JRE-Version(s)** 9, 10

## Hardware Requirements

### Native Images

**Requirements**

_Recommended_

- Storage: 150 MB
- Memory: 100 MB
- Processor: Any X64 bit processor
- Graphics: OpenGL Compatible (or X11 on Linux)
- Soundcard: Non specific

_Minimum_

- Storage: ~70 MB
- Memory: 50 MB (! Can be unstable !)
- Processor: Any X64 bit processor
- Graphics: OpenGL Compatible
- Soundcard: Non specific

### JAR-With precompiled native libraries

**Requirements**

_Recommended_

- Storage: 80 MB
- Memory: Dependent on JRE being used
- Processor: Dependent on JRE being used
- Graphics: OpenGL compatible
- Soundcard: Non specific

_Minimum_

- Storage: ~50 MB
- Memory: Dependent on JRE being used
- Processor: Dependent on JRE being used
- Graphics: OpenGL Compatible or X11
- Soundcard: Non specific
