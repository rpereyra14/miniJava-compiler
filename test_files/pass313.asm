  0         JUMP         L11
  1  L10:   RETURN (0)   0
  2         LOADL        50
  3         RETURN (1)   1
  4  L11:   LOADL        -1
  5         LOADL        2
  6         LOADA        1[CB]
  7         LOADA        2[CB]
  8         LOADL        -1
  9         CALL         L10
 10         HALT   (0)   
