{
	"count": 5,
	"items": [
		{
			"id": 0,
			"title": "Item 1"
		},
		{
			"id": 1,
			"title": "Item 2"
		},
		{
			"id": 2,
			"title": "Item 3"
		},
		{
			"id": 3,
			"title": "Item 4"
		},
		{
			"id": 4,
			"title": "Item 5",
			"data": "32423423"
		}
	],
	"attrs": {
		"key1": "value1",
		"key2": "value2"
	}
}

>DESCRIBE '.count'
INTEGER

>DESCRIBE '.items'
ARRAY(OBJECT[4](INTEGER, STRING))

>DESCRIBE '.attrs'
OBJECT(STRING, STRING)

>DESCRIBE '.'
OBJECT(INTEGER, ARRAY(OBJECT[4](INTEGER, STRING)), OBJECT(STRING, STRING))

>GET '.items[0].id'
0

>GET '.attrs[1]'
value2

>GET '.attrs.key2'
value2

>GET '.attrs.key2' AS JSON
"value2"

>GET '.attrs.key2' AS JSON WITH KEYS
{ "key2": "value2" }

>GET '.attrs.key2' WITH KEYS
key2 -> value2

>LIST '.count'
5

>GET '.count'
5

>GET '.items.id'
0

>GET '.items.id' SKIP 1
1

>LIST '.items.id'
0
1
2
3
4

>LIST '.items.id' LIMIT 2
0
1

>LIST '.items.id' LIMIT 2
0
1

>LIST '.items.title' WHERE '.items.id' > 2
Item 4
Item 5

>GET '.attrs' AS JSON
{
	"key1": "value1",
	"key2": "value2"
}

>GET '.attrs'
key1 -> value1
key2 -> value2

>LIST '.items' AS JSON
[
	{
		"id": 0,
		"title": "Item 1"
	},
	{
		"id": 1,
		"title": "Item 2"
	},
	{
		"id": 2,
		"title": "Item 3"
	},
	{
		"id": 3,
		"title": "Item 4"
	},
	{
		"id": 4,
		"title": "Item 5",
		"data": "32423423"
	}
]

>LIST '.items[id, title]' AS JSON
[
	{
		"id": 0,
		"title": "Item 1"
	},
	{
		"id": 1,
		"title": "Item 2"
	},
	{
		"id": 2,
		"title": "Item 3"
	},
	{
		"id": 3,
		"title": "Item 4"
	},
	{
		"id": 4,
		"title": "Item 5"
	}
]

>LIST '.items(id, title)'
id, title
0, Item 1
1, Item 2
2, Item 3
3, Item 4
4, Item 5

>LIST KEYS '.attrs'
key1
key2

>LIST VALUES '.attrs' AS JSON
["value1", "value2"]

>LIST VALUES '.attrs' AS JSON WITH KEYS
{"key1": "value1", "key2": "value2"}

>GET '.attrs' AS JSON WITH KEYS
{
	"attrs" : {
		"key1": "value1",
		"key2": "value2"
	}
}

>GET '.attrs' WITH KEYS
"attrs" : {
	"key1": "value1",
	"key2": "value2"
}