# Notes

## 1/13/26
General discussion on the history of Java. It is a hybrid language that compiles into a JavaByte is the interpreted on
specific devices. Can be faster the C/C++ as it can dynamically compile certain code during runtime, rather than having
to guess in advance.

Also went over Phase 0 and getting started.

## 1/15/26
For phase0, don't worry about check/checkmate. You also don't need to worry about en passant or castling.

Put all the common logic in the parent move calculator class.

In the move calculator, use the ChessPosition to find the piece type at that position on ChessBoard to create the
correct subclass of move calculator.

### Using Java from cmd

- Compile:
  - javac {name}.java
  - Produces {name}.class
  - Be in the directory of your java file
- Run:
  - java {name}.class

### JavaDoc
Example of documentation:
```java
/**
 * Description of class.
 * Additional description/examples
 */
public class MyClass{ 
    /**
     * Description of method.
     * Additional description/examples
     * @param ...
     * @return ...
     */
     public void myMethod() {
        
    }
}
```

### Primitive Datatypes

- byte (1 byte)
- short (2 bytes)
- __int__ (4 bytes)
- long (8 bytes)
- float (4 bytes)
- __double__ (8 bytes)
- char (2 bytes)
- __boolean__

#### Integrals
Literal integral primitives are always interpreted as `int`.
```java
long long1 = 10;
long long2 = 10L;
```
Because of this, `long1` actually takes slightly longer to initialize than `long2`.

#### Floats
Floating point literals are always interpreted as `double`.
```java
float aFloat = 2.5;
float bFloat = 2.5f;
double aDouble = 3.14;
```
`aFloat` will not compile, as doubles need to be cast to floats before being assigned to a float variable.

#### Converting Between Primitives and Strings

```java
int i = new Integer.parseInt(string);
```

#### Strings
- Declaration and Assignment
```java
String s1 = "Hello";
String s2 = new String("Hello");
```
Don't use `new` with strings
- Concatenation
```java
String s1 = "Hello";
String s2 = "BYU";
String s3 = s1 + " " + s2;
```
Inefficient for many strings, as it makes a new string for each step of the concatenation.
- Formatting
```java
String s1 = "Hello";
String s2 = "BYU";
String s3 = String.format("%s %s", s1, s2);
```

- Special Characters
  - `\n` newline
  - `\t` tab
  - `\"` double quote
  - `\'` single quote
  - `\\` backslash
  - `\b` backspace
  - `\uXXXX` Unicode for hex XXXX
  - `\r` carriage return
  - `\f` form feed


- String Builder

More efficient way to create strings from many parts

```java
public class Example {
  public static void main(String[] args) {
    StringBuilder builder = new StringBuilder();
    builder.append("Using ");
    builder.append("a ");
    builder.append("StringBuilder ");
    builder.append("is more efficient.");
    String str = builder.toString();
  }
}
```

## 1/20/26
Alma 34:24-25: Cry unto the Lord always, he will help your with whatever your flocks and your fields may be.

### Arrays
In Java, multidimensional arrays are arrays of arrays. The main difference is that multidimensional arrays in Java can be ragged. This means that inner arrays can be different sizes.

### Packages
- Way to organize classes into logical groups
- Packages can have sub-packages
- Specify the package for a class with a 'package' statement at the top of the .java file
- Files must be in a directory structure that matches the package path
- The convention is to store your packages in a path that is the reverse of a domain you own
- Many default Java packages do not follow this convention, and are instead stored in a java directory

### Import
- Provide a shorthand for the full package name (e.g. `import edu.byu.cs.Student`)
- You do not need to import for the following:
  - You for some reason want to use the full package name
  - The class you are using is java.lang (imported by default, has Object class etc.)
  - The class you would import is in the same package as the class that needs to use it

### CLASSPATH
- Environment variable that contains a list of directories that contain .class files
- Current directory is implicitly on the CLASSPATH
- Can use -classpath command line parameter

### Classes and Objects
- Classes are templates and objects are the things we interact with
- References act like pointers, and they are addresses to objects. But just because we have a reference, doesn't mean we have an object.
```java
import java.utils.Date;

public class ExampleClass {
    public void exampleMethod() {
        //Create reference
        Date dt;
        //Assign an object to the reference
        dt = new Date();
    }
}
```
- Reference equality is different that object equality
  - Reference equality just looks at where the reference points
  - Object equality, by default, does the same thing as reference equality, but you can override the method so you can define what equality means

### Instance vs Static Variables
- Instance Variable
  - Each object receives its own copy of instance variables
  - Most variables should be instance variables
- Static Variables
  - Really should be called class variables. Only one per class
  - Use in special cases where you won't create a new instance, or if there is only one value

### Instance vs Static Methods
- Instance Methods
  - Methods are associated with a specific instance
  - Invoked from a reference to an instance
  - When invoked by an instance, it acts on that instance's variables
  - Can also access that class's static variables and methods
- Static Methods
  - Methods are associated with a class
  - Invoked by using the class name
  - Can be invoked from a reference, but the method is still not associated with an instance 
  - Cannot access instance variables or methods

### Getters and Setters
- Used to control how users interact with you instance/static variables
- Can use Intellij to generate getters and setters. This is actually better practice.