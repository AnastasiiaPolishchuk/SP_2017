# Event structure
|  scope |  action id |  message id  |  data type |  data   |
|--------|------------|--------------|------------|---------|
| 1 Byte |   4 Byte   |   4 Byte     |   1 Byte   | n Bytes |


### Scope

| code | description                     |
|------|---------------------------------|
| 0x00 | event affects whole game        |
| 0x01 | event affects player 1          |
| 0x02 | event affects player 2          |
| ...  | ...                             |
| 0xFF | event affects player 255        |

### Action

|     id     |    scope      |  data   |description                     |
|------------|---------------|---------|--------------------------------|
| 0x00000000 | game          | empty   | message log only               |
| 0x00000001 | game          | string  | player joins game              |
| 0x00000002 | game          | string  | player leaves game             |
| 0x00000003 | game          | string  | player disconnected            |
| 0x00000004 | game          


### Data type
| code | description                     |
|------|---------------------------------|
| 0x00 | empty                           |
| 0x01 | integer                         |
| 0x02 | string                          |