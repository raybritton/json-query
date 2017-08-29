
See spec file for possible usage

`METHOD TARGET TARGET_MODIFIER WHERE_EXPR SKIP LIMIT WITH_KEYS AS_JSON PRETTY`

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
`WHERE target FROM array OPERATOR value`
* WHERE "name" FROM ".people" # "John"
* WHERE "meta.key1" FROM ".people" !# "VALID"
* WHERE "id" FROM "." > 0
* WHERE "title" FROM "." == "New"
* WHERE ELEMENT FROM ".grade.ages" < 20

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

##### PRETTY:
* PRETTY

Examples:

`DESCRIBE "."`

`SELECT ".items" WHERE ".items.id" > 5 AS JSON`

`SELECT "name" FROM ".people" LIMIT 10 WITH KEYS`

`SELECT KEYS FROM ".people2 SKIP 6`