  0         JUMP         L11
  1  L10:   RETURN (0)   0
  2  L11:   LOADL        -1
  3         LOADL        1
  4         LOADA        1[CB]
  5         JUMP         L13
  6  L12:   RETURN (0)   1
  7  L13:   LOADL        -1
  8         LOADL        1
  9         LOADA        6[CB]
 10         LOADL        -1
 11         CALL         L10
 12         HALT   (0)   
