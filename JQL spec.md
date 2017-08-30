
`METHOD DISTINCT TARGET_MODIFIER TARGET WHERE_EXPR OFFSET LIMIT WITH_KEYS AS_JSON PRETTY ORDER BY DESC`

##### METHOD:
* DESCRIBE
* SELECT

###### DISTINCT
* DISTINCT

##### TARGET:
* '.'
* '.items'
* '.items.id'
* '.items[0]'
* '.items[0].inner.title'

##### TARGET_MODIFIER:
* KEYS FROM
* VALUES FROM
* ("id", "title") FROM
* "id" FROM

##### WHERE_EXPR:
`WHERE target OPERATOR value`
* WHERE "name" # "John"
* WHERE "meta.key1" !# "VALID"
* WHERE "id" > 0
* WHERE "title" == "New"
* WHERE ELEMENT < 20

###### OPERATORS
* == EQUAL
* != NOT EQUAL
* < LESS THAN
* \> GREATER THAN
* \# CONTAINS
* !# NOT CONTAINS

##### OFFSET:
* OFFSET 1

##### LIMIT:
* LIMIT 10

##### WITH_KEYS:
* WITH KEYS

##### AS_JSON:
* AS JSON

##### PRETTY:
* PRETTY

##### ORDER_BY
* ORDER BY ELEMENT
* ORDER BY "id"

##### DESC
* DESC

Examples:

`DESCRIBE "."`

`SELECT ".items" WHERE ".items.id" > 5 AS JSON`

`SELECT "name" FROM ".people" LIMIT 10 WITH KEYS`

`SELECT KEYS FROM ".people2 OFFSET 6`