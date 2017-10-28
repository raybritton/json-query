# JQL

#### Json Query Language

JQL is designed to be used to query json so that values, objects or arrays can be extracted based on filters and limits.

Note: all numbers are output as decimals as JSON only has one number type: double.

<pre>
SELECT | DESCRIBE
    [DISTINCT]
    [[KEYS|VALUES|column|columns|mathExpr] FROM]
    jsonPath
    [WHERE column operator value]
    [LIMIT value]
    [OFFSET value]
    [WITH KEYS]
    [AS JSON]
    [PRETTY]
    [ORDER BY column [DESC]]
</pre>

<pre>
SEARCH
    jsonPath
    FOR
    KEY | VALUE
    value
</pre>

#### Components

* SELECT | DESCRIBE
    * `SELECT` returns values from the data
    * `DESCRIBE` returns a description of the data
* DISTINCT
    * This is used to only allow distinct values to be returned
* mathExpr 
    * `MAX(ELEMENT|column)`
    * `MIN(ELEMENT|column)`
    * `COUNT(ELEMENT|column)`
    * `SUM(ELEMENT|column)`
* KEYS | VALUES | column | columns | mathExpr
    * `KEYS` only returns the keys from an object
    * `VALUES` only returns the values from an object
    * column should be written as `"id"`
    * columns should be written as `("id", "name")`
    * All of these be followed by `FROM`
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
    * column should be written as `"id"`
        * To refer to list element use `ELEMENT`
    * operator:
        * `==` Equal
        * `!=` Not equal
        * `>` Greater than
        * `<` Less than
        * `#` Contains
        * `!#` Not contains
    * value should be written as `1` or `"a"`
* LIMIT
    * This only is used if the target is an array
    * This is the maximum number of results
    * value must be a positive integer
* OFFSET
    * This only is used if the target is an array
    * This is the number of results that will be skipped
    * value must be a positive integer
* WITH KEYS
    * By default only the values are returned, this caused the key to be returned as well
    * This only works with SELECT
* AS JSON
    * This will return the filtered data in the JSON format
    * This only works with SELECT
* PRETTY
    * By default the JSON (from AS JSON) is returned on a single line, this causes it to be pretty printed
    * This also works with DESCRIBE
* ORDER BY
    * column should be written as `"id"`
        * To refer to list element use `ELEMENT`
    * DESC reverses the sort order

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

`SELECT "."`

`[{0.0, John Smith}, {1.0, Emma Smith}, {2.0, Jane Clobber}, {3.0, Ned Turner}]`

###### Select all results with an id greater than 1

`SELECT "." WHERE "id" > 1`

`[{2.0, Jane Clobber}, {3.0, Ned Turner}]`

###### Select first result whose name contains "Jane"

`SELECT "." WHERE "name" # "Jane"`

`{2.0, Jane Clobber}`

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