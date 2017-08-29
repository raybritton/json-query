# JSON Query Library (WIP)

A library for filtering json to extract parts from a structure.

For example:
* To list ids from a list of people
    * `SELECT "id" FROM ".people"`
* To list names from a list of people older than 20
    * `SELECT "full_name" FROM ".people" WHERE "age" > 20`
* To list person object from a list of people name contains "Dr"
    * `SELECT ELEMENT FROM ".people" WHERE "full_name" # "Dr"`
    
See [SYSTEM](https://github.com/raybritton/json-query/blob/master/SYSTEM.md) for more information

### CLI

* -q query
* -i input file, json or url

`java -jar jq-0.1.0.jar -i "example.json" -q "LIST \"id\" from \".people\""`

## Download

Add a Gradle dependency:

```groovy
compile 'com.raybritton.jsonquery:lib:0.3.0'
```

A CLI JAR is available from the [releases page](https://github.com/raybritton/json-query/releases)

## License

```
Copyright 2017 Ray Britton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
