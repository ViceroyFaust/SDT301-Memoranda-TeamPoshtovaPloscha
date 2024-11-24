*For questions that aren't addressed here, please contact Vladyslav.*

# XMLFramework
This is a documentation for end-user of XMLFramework. It doesn't cover the internal fuctional of it nor expects user to understand it.  
After reading this documentation user is expected to understand the basics needed to work with XMLFramework and how to add new models to it.  

## Index
- Introduction to the XMLFramework
- Creating models designed to use with ApplicationContext
- Adding new sets to ApplicationContext
- Manipulating with the existings sets

# Introduction to the XMLFramework
XMLFramework is designed to be user friendly mapping framework for XML. The entry point for XMLFramework is *ApplicationContext* class. It stores the *ModelsSubset*. The *ModelsSubset* is the generic class that stores the models.  
It has these features:  
- Enforced unique id
- Autoincrement of id
- Support for models that extends XMLAble and IModel interfaces
- Parsing from XML
- Saving to XML
- Foreign Keys support  
**What you need to know:** *ModelsSubset* is a class that contains the models and perfoms basic operations with them (add,delete,etc) it **autoincrements id** that means that you should **never manually set id for the model**.  
**ApplicationContext**  
This is your main class that you will be working with. It stores the *ModelsSubsets* and supports save and loading functionality.   

# Creating models designed to use with ApplicationContext

# Adding new sets to ApplicationContext

# Manipulating with the existings sets
- To access your models in the context you should use ApplicationContext 
```java
ApplicationContext applicationContext  = new ApplicationContext();
``` 
- To add new object to the context subset you can use
```java
Driver driver = new Driver( "John Doe", "1234567890");
modelsContext.drivers.add(driver);
```  
This will add a new driver to our context

- To create realtionship beetwen objects you can do  
```java
Driver driver = new Driver( "John Doe", "1234567890");
modelsContext.drivers.add(driver);
modelsContext.buses.add(new Bus(10, driver));
modelsContext.buses.add(new Bus(14, driver));
```
this code will create a relationship beetwen bus and driver. It can be furtherly accessed with bus.driver
**Important!** because the id is auto generated you can't assign the relationship before loading it into the context. For example:
```java
Driver driver = new Driver( "John Doe", "1234567890");
modelsContext.buses.add(new Bus(10, driver));
modelsContext.buses.add(new Bus(14, driver));
modelsContext.drivers.add(driver);
```
This snippet of code adds driver to the context after it was added to the busses. It means that id for the driver wasn't generated and relationship won't be creted.
You should **always** add the model to the context and only after use it anywhere else.

- Saving. To save the context simply use 
```java
modelsContext.save(filePath)
```

- Loading. To load the context simply use
```java
modelsContext.load(filePath)
```
This command will load the subsets and will perform lazy loading
