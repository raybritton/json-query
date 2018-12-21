package com.raybritton.jsonquery.file

import com.raybritton.jsonquery.JsonQuery
import org.junit.Assert.assertEquals
import org.junit.Test

class JsonFile1Tests {
    private val json = """
        [
  {
    "_id": "5c1bc83c3162d24cb98986b5",
    "index": 0,
    "guid": "5e33e400-735e-4e39-b92a-fbece7c5b10b",
    "isActive": true,
    "balance": "1722.25",
    "picture": "http://placehold.it/32x32",
    "age": 39,
    "eyeColor": "green",
    "name": {
      "first": "Hendrix",
      "last": "Lloyd"
    },
    "company": "CYCLONICA",
    "email": "hendrix.lloyd@cyclonica.biz",
    "phone": "+1 (933) 406-3704",
    "address": "328 Goodwin Place, Dargan, Colorado, 1891",
    "about": "Commodo ullamco eiusmod est reprehenderit veniam mollit cillum. Commodo minim irure amet laborum ea do. Voluptate in ea nostrud labore dolore. Tempor aliqua esse id anim. Ullamco consectetur tempor amet sit.",
    "registered": "Wednesday, January 18, 2017 8:56 PM",
    "latitude": "-89.680277",
    "longitude": "-7.256303",
    "tags": [
      "cupidatat",
      "ullamco",
      "id",
      "cillum",
      "ea"
    ],
    "range": [
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9
    ],
    "friends": [
      {
        "id": 0,
        "name": "Jayne Elliott"
      },
      {
        "id": 1,
        "name": "Lauren Dillard"
      },
      {
        "id": 2,
        "name": "Skinner Faulkner"
      }
    ],
    "greeting": "Hello, Hendrix! You have 5 unread messages.",
    "favoriteFruit": "banana"
  },
  {
    "_id": "5c1bc83cbe473abd55f2bf7b",
    "index": 1,
    "guid": "9940d205-5559-46ef-97d8-71b4a0aa6677",
    "isActive": true,
    "balance": "1488.29",
    "picture": "http://placehold.it/32x32",
    "age": 32,
    "eyeColor": "blue",
    "name": {
      "first": "Benson",
      "last": "Horn"
    },
    "company": "ZIZZLE",
    "email": "benson.horn@zizzle.biz",
    "phone": "+1 (877) 552-3635",
    "address": "645 Ralph Avenue, Suitland, New Jersey, 1275",
    "about": "Consectetur cupidatat est dolore consequat id. Dolore consequat dolor deserunt tempor velit tempor amet nisi ipsum excepteur labore non commodo. Adipisicing anim ut aliquip nostrud sunt qui veniam duis laboris laborum magna qui excepteur. Est officia occaecat duis enim ipsum nisi consequat anim esse enim tempor. Amet laborum incididunt voluptate minim exercitation magna. Velit nostrud adipisicing eiusmod non voluptate. Officia officia labore Lorem pariatur enim consectetur eiusmod sint nostrud.",
    "registered": "Thursday, December 4, 2014 3:23 AM",
    "latitude": "-62.471112",
    "longitude": "-96.020137",
    "tags": [
      "eiusmod",
      "eu",
      "anim",
      "pariatur",
      "sit"
    ],
    "range": [
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9
    ],
    "friends": [
      {
        "id": 0,
        "name": "Adrienne Hicks"
      },
      {
        "id": 1,
        "name": "Burns Lowery"
      },
      {
        "id": 2,
        "name": "Gina Hanson"
      }
    ],
    "greeting": "Hello, Benson! You have 9 unread messages.",
    "favoriteFruit": "apple"
  },
  {
    "_id": "5c1bc83c96ab290815553fb3",
    "index": 2,
    "guid": "f52e4cbe-4559-468d-98af-f7df886a3d6e",
    "isActive": false,
    "balance": "3,401.33",
    "picture": "http://placehold.it/32x32",
    "age": 24,
    "eyeColor": "blue",
    "name": {
      "first": "Gray",
      "last": "Campbell"
    },
    "company": "KROG",
    "email": "gray.campbell@krog.name",
    "phone": "+1 (826) 400-2957",
    "address": "791 Temple Court, Lithium, Wyoming, 4830",
    "about": "Quis voluptate qui fugiat reprehenderit ut. Cupidatat mollit excepteur officia culpa ex id labore ad nisi. Cupidatat enim aute esse amet adipisicing cupidatat ad nisi sit exercitation. Minim velit ut consectetur consectetur proident ex qui esse. Est enim magna ea aliquip culpa minim eu. Enim do qui enim sit pariatur duis.",
    "registered": "Saturday, May 2, 2015 3:31 PM",
    "latitude": "65.941232",
    "longitude": "94.028166",
    "tags": [
      "dolore",
      "minim",
      "adipisicing",
      "tempor",
      "culpa"
    ],
    "range": [
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9
    ],
    "friends": [
      {
        "id": 0,
        "name": "Barrera Ochoa"
      },
      {
        "id": 1,
        "name": "Courtney Mcconnell"
      },
      {
        "id": 2,
        "name": "Allison Stark"
      }
    ],
    "greeting": "Hello, Gray! You have 6 unread messages.",
    "favoriteFruit": "banana"
  },
  {
    "_id": "5c1bc83c80fc0ac010b4cf8a",
    "index": 3,
    "guid": "bfdbfd90-ae1d-4b45-a2b5-480a300e500c",
    "isActive": true,
    "balance": "2927.39",
    "picture": "http://placehold.it/32x32",
    "age": 26,
    "eyeColor": "blue",
    "name": {
      "first": "Johnston",
      "last": "Stephens"
    },
    "company": "LYRIA",
    "email": "johnston.stephens@lyria.net",
    "phone": "+1 (965) 436-2864",
    "address": "562 Clara Street, Crisman, Georgia, 7753",
    "about": "Anim velit ut mollit eiusmod nisi voluptate voluptate. Consequat quis id sint enim dolor sit ea irure. Ex et consequat dolore qui eu sunt excepteur cupidatat magna elit ut sit quis. Cupidatat anim anim do irure sint ex dolor. Elit irure proident proident aute magna nostrud.",
    "registered": "Wednesday, July 13, 2016 6:41 PM",
    "latitude": "-32.409937",
    "longitude": "128.342151",
    "tags": [
      "ea",
      "excepteur",
      "ullamco",
      "enim",
      "eu"
    ],
    "range": [
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9
    ],
    "friends": [
      {
        "id": 0,
        "name": "Barron Snow"
      },
      {
        "id": 1,
        "name": "Kris Mcintosh"
      },
      {
        "id": 2,
        "name": "Blevins Lott"
      }
    ],
    "greeting": "Hello, Johnston! You have 9 unread messages.",
    "favoriteFruit": "apple"
  },
  {
    "_id": "5c1bc83ce9e467612df8361a",
    "index": 4,
    "guid": "b9166636-c5bf-4b80-a3d9-f9024c828275",
    "isActive": true,
    "balance": "1606.99",
    "picture": "http://placehold.it/32x32",
    "age": 40,
    "eyeColor": "brown",
    "name": {
      "first": "Vera",
      "last": "Castillo"
    },
    "company": "BIZMATIC",
    "email": "vera.castillo@bizmatic.io",
    "phone": "+1 (923) 528-3190",
    "address": "536 Court Street, Robinson, New Mexico, 548",
    "about": "Cupidatat proident laboris anim laboris voluptate reprehenderit aute tempor aute amet et elit labore sit. Cupidatat anim sit qui nostrud incididunt sint ullamco qui qui id do deserunt aute ipsum. Mollit sint do elit amet non sit ullamco veniam occaecat magna ea excepteur. Nulla enim fugiat et voluptate non mollit aliqua nostrud quis amet reprehenderit.",
    "registered": "Monday, March 3, 2014 12:50 PM",
    "latitude": "5.788265",
    "longitude": "58.632485",
    "tags": [
      "laborum",
      "consequat",
      "cupidatat",
      "est",
      "irure"
    ],
    "range": [
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9
    ],
    "friends": [
      {
        "id": 0,
        "name": "Kemp Barnett"
      },
      {
        "id": 1,
        "name": "Allison Perry"
      },
      {
        "id": 2,
        "name": "Mclaughlin Paul"
      }
    ],
    "greeting": "Hello, Vera! You have 5 unread messages.",
    "favoriteFruit": "strawberry"
  },
  {
    "_id": "5c1bc83c99898e42f7f0aca1",
    "index": 5,
    "guid": "0381d41b-f26c-44a7-92e2-7fe81606945e",
    "isActive": true,
    "balance": "3533.74",
    "picture": "http://placehold.it/32x32",
    "age": 35,
    "eyeColor": "green",
    "name": {
      "first": "Kaufman",
      "last": "Sargent"
    },
    "company": "PARCOE",
    "email": "kaufman.sargent@parcoe.us",
    "phone": "+1 (993) 514-2554",
    "address": "296 Williams Avenue, Brenton, Massachusetts, 7551",
    "about": "Fugiat sit velit consectetur sint voluptate fugiat ex. Adipisicing reprehenderit culpa nisi elit. Dolore sint aute voluptate proident reprehenderit quis labore excepteur laborum.",
    "registered": "Monday, October 29, 2018 7:51 AM",
    "latitude": "9.53585",
    "longitude": "30.503145",
    "tags": [
      "dolor",
      "aute",
      "duis",
      "do",
      "incididunt"
    ],
    "range": [
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9
    ],
    "friends": [
      {
        "id": 0,
        "name": "Claudia Dalton"
      },
      {
        "id": 1,
        "name": "Cooke Mendez"
      },
      {
        "id": 2,
        "name": "Cheryl Johnson"
      }
    ],
    "greeting": "Hello, Kaufman! You have 10 unread messages.",
    "favoriteFruit": "strawberry"
  },
  {
    "_id": "5c1bc83cc548913657a75453",
    "index": 6,
    "guid": "c0741242-0328-4e35-a89c-ea2a62d79a08",
    "isActive": false,
    "balance": "2923.68",
    "picture": "http://placehold.it/32x32",
    "age": 34,
    "eyeColor": "green",
    "name": {
      "first": "Angeline",
      "last": "Head"
    },
    "company": "NETPLAX",
    "email": "angeline.head@netplax.tv",
    "phone": "+1 (802) 506-2260",
    "address": "215 Withers Street, Wyoming, Nevada, 5622",
    "about": "Aliqua cupidatat incididunt labore laboris consectetur est eu consequat eu dolor ullamco mollit aliqua. Aliqua ullamco sit eu ex proident voluptate aliqua ea duis anim deserunt. Eiusmod sunt fugiat fugiat culpa fugiat cillum cupidatat minim duis. Voluptate veniam et ut mollit proident minim excepteur officia occaecat in adipisicing veniam voluptate. Et esse dolore occaecat labore et labore anim. Aliqua enim enim fugiat commodo esse ipsum reprehenderit velit. Dolore velit ullamco aliquip id et aute nisi.",
    "registered": "Saturday, September 15, 2018 5:50 PM",
    "latitude": "64.978899",
    "longitude": "140.007399",
    "tags": [
      "sunt",
      "dolore",
      "amet",
      "do",
      "anim"
    ],
    "range": [
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9
    ],
    "friends": [
      {
        "id": 0,
        "name": "Natasha Lyons"
      },
      {
        "id": 1,
        "name": "Boone Doyle"
      },
      {
        "id": 2,
        "name": "Celina Gomez"
      }
    ],
    "greeting": "Hello, Angeline! You have 9 unread messages.",
    "favoriteFruit": "banana"
  },
  {
    "_id": "5c1bc83c7e7ea9094620d4bb",
    "index": 7,
    "guid": "679a67f8-5c4e-49f0-ae2e-be5f2e0d055e",
    "isActive": false,
    "balance": "2741.51",
    "picture": "http://placehold.it/32x32",
    "age": 21,
    "eyeColor": "blue",
    "name": {
      "first": "Carey",
      "last": "Hayden"
    },
    "company": "ACRUEX",
    "email": "carey.hayden@acruex.info",
    "phone": "+1 (983) 514-2424",
    "address": "783 Ocean Court, Wilsonia, Marshall Islands, 2397",
    "about": "Culpa anim enim voluptate laborum excepteur sint. Laboris sit commodo magna deserunt laborum ut ullamco elit laboris tempor irure deserunt. Tempor cupidatat id sit minim qui sunt et nisi laborum irure reprehenderit fugiat. Officia ipsum veniam id laboris aute dolore occaecat officia fugiat dolor. Laboris tempor exercitation sunt qui. Quis tempor aliquip adipisicing magna sunt sunt laborum ipsum est cillum duis dolor Lorem incididunt.",
    "registered": "Friday, April 8, 2016 4:54 AM",
    "latitude": "-70.091751",
    "longitude": "146.178257",
    "tags": [
      "velit",
      "ut",
      "reprehenderit",
      "fugiat",
      "aute"
    ],
    "range": [
      0,
      1,
      2,
      3,
      4,
      5,
      6,
      7,
      8,
      9
    ],
    "friends": [
      {
        "id": 0,
        "name": "Mckay Lamb"
      },
      {
        "id": 1,
        "name": "Lucia Copeland"
      },
      {
        "id": 2,
        "name": "Mcfarland Steele"
      }
    ],
    "greeting": "Hello, Carey! You have 7 unread messages.",
    "favoriteFruit": "banana"
  }
]
    """.trimIndent()

    private val jsonQuery = JsonQuery().loadJson(json)

    @Test
    fun listAllNameFields() {
        val output = jsonQuery.query("SEARCH '.' FOR KEY 'name'")

        assertEquals("Name fields", ".[0].name\n" +
                ".[0].friends.[0].name\n" +
                ".[0].friends.[1].name\n" +
                ".[0].friends.[2].name\n" +
                ".[1].name\n" +
                ".[1].friends.[0].name\n" +
                ".[1].friends.[1].name\n" +
                ".[1].friends.[2].name\n" +
                ".[2].name\n" +
                ".[2].friends.[0].name\n" +
                ".[2].friends.[1].name\n" +
                ".[2].friends.[2].name\n" +
                ".[3].name\n" +
                ".[3].friends.[0].name\n" +
                ".[3].friends.[1].name\n" +
                ".[3].friends.[2].name\n" +
                ".[4].name\n" +
                ".[4].friends.[0].name\n" +
                ".[4].friends.[1].name\n" +
                ".[4].friends.[2].name\n" +
                ".[5].name\n" +
                ".[5].friends.[0].name\n" +
                ".[5].friends.[1].name\n" +
                ".[5].friends.[2].name\n" +
                ".[6].name\n" +
                ".[6].friends.[0].name\n" +
                ".[6].friends.[1].name\n" +
                ".[6].friends.[2].name\n" +
                ".[7].name\n" +
                ".[7].friends.[0].name\n" +
                ".[7].friends.[1].name\n" +
                ".[7].friends.[2].name", output)
    }

    @Test
    fun listAllNames() {
        val output = jsonQuery.query("SEARCH '.' FOR KEY 'name' WITH VALUES")

        assertEquals("All names", ".[0].name: {first=Hendrix, last=Lloyd}\n" +
                ".[0].friends.[0].name: Jayne Elliott\n" +
                ".[0].friends.[1].name: Lauren Dillard\n" +
                ".[0].friends.[2].name: Skinner Faulkner\n" +
                ".[1].name: {first=Benson, last=Horn}\n" +
                ".[1].friends.[0].name: Adrienne Hicks\n" +
                ".[1].friends.[1].name: Burns Lowery\n" +
                ".[1].friends.[2].name: Gina Hanson\n" +
                ".[2].name: {first=Gray, last=Campbell}\n" +
                ".[2].friends.[0].name: Barrera Ochoa\n" +
                ".[2].friends.[1].name: Courtney Mcconnell\n" +
                ".[2].friends.[2].name: Allison Stark\n" +
                ".[3].name: {first=Johnston, last=Stephens}\n" +
                ".[3].friends.[0].name: Barron Snow\n" +
                ".[3].friends.[1].name: Kris Mcintosh\n" +
                ".[3].friends.[2].name: Blevins Lott\n" +
                ".[4].name: {first=Vera, last=Castillo}\n" +
                ".[4].friends.[0].name: Kemp Barnett\n" +
                ".[4].friends.[1].name: Allison Perry\n" +
                ".[4].friends.[2].name: Mclaughlin Paul\n" +
                ".[5].name: {first=Kaufman, last=Sargent}\n" +
                ".[5].friends.[0].name: Claudia Dalton\n" +
                ".[5].friends.[1].name: Cooke Mendez\n" +
                ".[5].friends.[2].name: Cheryl Johnson\n" +
                ".[6].name: {first=Angeline, last=Head}\n" +
                ".[6].friends.[0].name: Natasha Lyons\n" +
                ".[6].friends.[1].name: Boone Doyle\n" +
                ".[6].friends.[2].name: Celina Gomez\n" +
                ".[7].name: {first=Carey, last=Hayden}\n" +
                ".[7].friends.[0].name: Mckay Lamb\n" +
                ".[7].friends.[1].name: Lucia Copeland\n" +
                ".[7].friends.[2].name: Mcfarland Steele", output)
    }

    @Test
    fun listAllValuesWithCarey() {
        val output = jsonQuery.query("SEARCH '.' FOR VALUE 'Carey'")

        assertEquals("Careys", ".[7].name.first", output)
    }

    @Test
    fun listAllPeopleWhoAreFriendsWithAnAllison() {
        val output = jsonQuery.query("SELECT ('name.first','name.last') FROM '.' WHERE 'friends.name' # 'Allison' ")

//        assertEquals("Allisons friends", "", output)
    }
}