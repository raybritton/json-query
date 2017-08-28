
See spec file for possible usage

`METHOD TARGET TARGET_MODIFIER WHERE_EXPR SKIP LIMIT WITH_KEYS AS_JSON`

##### METHOD:
* DESCRIBE
* SELECT

##### TARGET:
* '.'
* '.items'
* '.items.id'
* '.items[0]'
* '.items[0].inner.title'

##### TARGET_MODIFIER:
* KEYS
* VALUES
* VALUES("id", "title")

##### WHERE_EXPR:
`WHERE target IN array OPERATOR value`
* WHERE "name" IN ".people" # "John"
* WHERE "meta.key1" IN ".people" !# "VALID"
* WHERE "id" IN "." > 0
* WHERE "title" IN "." == "New"
* WHERE ELEMENT IN ".grade.ages" < 20

###### OPERATORS
* == EQUAL
* != NOT EQUAL
* < LESS THAN
* \> GREATER THAN
* \# CONTAINS
* !# NOT CONTAINS

##### SKIP:
* SKIP 1

##### LIMIT:
* LIMIT 10

##### WITH_KEYS:
* WITH KEYS

##### AS_JSON:
* AS JSON

Examples:

`DESCRIBE "."`

`SELECT ".items" WHERE ".items.id" > 5 AS JSON`

`SELECT "name" IN ".people" LIMIT 10 WITH KEYS`

`SELECT KEYS IN ".people2 SKIP 6`