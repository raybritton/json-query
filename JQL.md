# JQL

#### Json Query Language

See the new [syntax documentation](https://jql.dokku-ray.app/docs) 

JQL is designed to be used to query json so that values, objects or arrays can be extracted based on filters.

Note: all numbers are output as decimals as JSON only has one number type: double.

<pre>
SELECT
    [DISTINCT]
    [(KEYS|VALUES|field|fields|mathExpr) FROM]
    target
    [BY ELEMENT]
    [WHERE field operator searchTerm [CASE SENSITIVE]]
    [LIMIT value]
    [OFFSET value]
    [ORDER BY field [DESC]]
    [AS JSON]
    [PRETTY]
</pre>

<pre>
DESCRIBE
    [DISTINCT]
    [(field|fields) FROM]
    searchTerm
    [WHERE field operator searchTerm [CASE SENSITIVE]]
    [LIMIT value]
    [OFFSET value]
    [PRETTY]
    [WITH KEYS]
</pre>

<pre>
SEARCH
    [DISTINCT]
    target
    FOR
    [(KEY | VALUE)]
    operator
    searchTerm
    [CASE SENSITIVE]
    [WITH VALUES]
</pre>

#### Components

* SELECT | DESCRIBE
    * `SELECT` returns values from the data
    * `DESCRIBE` returns a description of the data
* DISTINCT
    * This is used to only allow distinct values to be returned
* mathExpr 
    * `MAX(ELEMENT | jsonPath)`
    * `MIN(ELEMENT | jsonPath)`
    * `COUNT(ELEMENT | jsonPath)`
    * `SUM(ELEMENT | jsonPath)`
        * When using math expression the target (the string after FROM) must be an array
        * Non number values in the array are ignored
        * NaN will be returned for MIN, MAX and SUM if the array contains no numbers
* KEYS | VALUES | field | fields | mathExpr
    * `KEYS` only returns the keys from an object
    * `VALUES` only returns the values from an object
    * field must be written as `"id"`
    * fields must be written as `("id", "name")`
    * All of these be followed by `FROM`
* target
    * jsonPath 
    * Or the results of a nested SELECT
    * All paths are relative to this, any sibling or parent of the target are not accessible in the query
* jsonPath
    * Parts of the json can be specified using a path
    * Each segment should be separated by a `.` (Fullstop/Period)
    * Any name containing `.`, `"` and `[` must be escaped using a back slash, for example `\.`
    * A first full stop represents the whole object
        * `"."` The whole object
        * `".items"` The items array or object on the root
        * `".ids[0]"` The first object in the ids array
        * `".items.id"` The id object or array in the items object
* WHERE
    * field should be written as `"id"`
        * To refer to list element use `ELEMENT`
    * operator:
        * `==` Equal
        * `!=` Not equal
        * `>` Greater than
        * `<` Less than
        * `>=` Greater than or equal
        * `<=` Less than or equal
        * `#` Contains (array, object, or string) 
        * `!#` Not contains (array, object, or string)
        
        (with objects contains checks for keys existence (`"key": null` counts as existing))
    * operator:
        * `IS` type checking: may be followed by NOT then must be followed by STRING, NUMBER, BOOLEAN, OBJECT, ARRAY or NULL
* LIMIT
    * This only is used if the target is an array
    * This is the maximum number of results
    * value must be a positive integer
* OFFSET
    * This only is used if the target is an array
    * This is the number of results that will be skipped
    * value must be a positive integer
* AS JSON
    * This will return the filtered data in the JSON format
    * This only works with SELECT
* PRETTY
    * By default the JSON (from AS JSON) is returned on a single line, this causes it to be pretty printed
    * This works with SELECT OR DESCRIBE
* ORDER BY
    * field should be written as `"id"`
        * To refer to list element use `ELEMENT`
    * DESC reverses the sort order
    * This only works with SELECT
* CASE SENSITIVE
    * Equals, contains and search are case insensitive by default, adding this makes them case sensitive
* searchTerm
    * A string, number, 'TRUE' or 'FALSE'
    * Or the results of a nested SELECT
* BY ELEMENT
    * Splits the results by their position in the target
    * If the json was an array of two objects both with an array of numbers inside i.e `[{numbers: [1,2,3], numbers: [5,6,10]}]` and the query was `SELECT SUM('numbers') FROM '.'` the result would be `27` but with `BY ELEMENT` it becomes `[6,21]`
    
* WITH KEYS
    * Includes the keys with the types
    * This only works with DESCRIBE     

* SEARCH
    * Search json data for a searchTerm
* KEY | VALUE | ANY
    * Search can look at just keys, values or both
* WITH VALUES
    * Print found value with location

Nested queries:

Only SELECTs can be used inside other queries and they must be surrounded by parenthesis and for
 
* `target` use AS JSON
* `searchTerm` not use AS JSON

i.e.

`DESCRIBE 'name' FROM (SELECT 'person' FROM '.' WHERE ... AS JSON) ...` 

#### Examples

Using:

<pre>
[
    {
        "id": 0,
        "name": "John Smith"
    },
    {
        "id": 1,
        "name": "Emma Smith"
    },
    {
        "id": 2,
        "name": "Jane Clobber"
    },
    {
        "id": 3,
        "name": "Ned Turner"
    }
]
</pre>

###### Select everything

`SELECT VALUES FROM "."`

`[{0.0, John Smith}, {1.0, Emma Smith}, {2.0, Jane Clobber}, {3.0, Ned Turner}]`

###### Select all results with an id greater than 1

`SELECT "." WHERE "id" > 1`

`[{id: 2.0, name: Jane Clobber}, {id: 3.0, name: Ned Turner}]`

###### Select first result whose name contains "Jane"

`SELECT "." WHERE "name" # "Jane"`

`{id: 2.0, name: Jane Clobber}`

###### Select ids 

`SELECT "id" FROM "."`

`[0.0, 1.0, 2.0, 3.0]`

###### Select last name as json 

`SELECT "name" FROM "." AS JSON LIMIT 1 ORDER BY "id" DESC`

`[{"name":"John Smith"}]`

###### Describe the data

`DESCRIBE "."`

`ARRAY(OBJECT(NUMBER, STRING)[4])`

###### Search for anything with the value "Ned Turner"

`SEARCH "." FOR VALUE "Ned Turner"`

`.[3].name: Ned Turner`
