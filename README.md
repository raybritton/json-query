# JSON Query Library (WIP)

See spec file for possible usage

`METHOD TARGET TARGET_MODIFIER WHERE_EXPR SKIP LIMIT WITH_KEYS AS_JSON`

##### METHOD:
* DESCRIBE
* GET
* LIST

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
* WHERE "id" > 0
* WHERE "title" == "New"

##### SKIP:
* SKIP 1

##### LIMIT:
* LIMIT 10

##### WITH_KEYS:
* WITH KEYS

##### AS_JSON:
* AS JSON

Examples:

`DESCRIBE '.'`

`LIST '.items' WHERE '.items.id' > 5 AS JSON`

`GET '.records' WITH KEYS`

`LIST '.people' VALUES("name") LIMIT 10`

`LIST '.people' KEYS SKIP 6`