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

## 1/22/26
Was 24 minutes late, missed instructions for the Programming Exam.

### Records
- A simplified type of class
- Automatically generates constructor, hash, toString, and getters
  - Getters are the variable names with no 'get' in front
- E.g.
- Can't directly modify the values stored in records, but can return a new record with a changed name
```java
public record Pet(int id, String name, String owner) {
    Pet rename(String newName) {
        return new Pet(id, newName, owner);
    }
}
```

### Exceptions and Exception Handling
- Exceptions and Errors are subclasses of Throwable
- Errors are things that you can't do anything about when they happen
  - OutOfMemory, NoClassDefFound, etc.
- Exceptions you can do stuff about
  - Bugs will throw exceptions, so the thing you can do is fix your code
    - NullPointerException, SegFault, etc.
  - Other exceptions can be handled in code, as they are the result of something unexpected, but shouldn't crash the program

#### Try/Catch Blocks
- Same idea as try/except in Python
```
// Without Multi-Catch
try {
  // Code that may throw an exception
} catch(SomeExceptionType ex) {
  // Code to handle the excption
} catch(OtherExceptionType) {
  // Code to handle the exception
}

// With Multi-Catch
try {
  // Code that may throw an exception
} catch(SomeExceptionType | OtherExceptionType ex) {
  // Code to handle the excption
}
```
- Catching, but not handling the exception is called "swallowing" the exception. This is not good
  - "It's the same as repentance, fix it, don't hide it" - Dr. Wilkerson

#### The Handle or Declare Rule
- Handle
```
try {
  ...
} catch {
  ...
}
```
- Declare
```
pubic void method() throws Exception {
  ...
}
```
- Only applies to checked exceptions. Errors and runtime exceptions (i.e. NullPointerException, etc.) cannot be declared and should not be handled
- Declaring that your method can throw a certain error means that you don't have to handle that exception
  - This means that everyone has to follow the handle or declare rule. If you don't handle the exception, you have to declare it
  - If you don't, the compiler with get angry at you
  - This allows more general classes to put off specific handling, and pass it on to whatever called it
- When you don't handle exceptions, your method gets popped off the stack

#### Finally
- The finally block comes after all of your catch blocks and is run after both try and catch blocks
- Used to clean things up, even if an exception is thrown

#### Try with Resources
- Makes it so you don't need a finally block most of the time
- Also makes it so you can easily throw the first exception that you got, rather than the most recent
  - e.g. Opening a corrupted file, then trying to close it in cleanup
  - Would throw an exception about closing the corrupted file
- Only works with some specific things that were made for try with resources

### Throwing Exceptions
- Don't make your exception early, as that will report the wrong line number
- You can make your own exceptions by extending the Exception class for checked exceptions, RuntimeException, or Error

