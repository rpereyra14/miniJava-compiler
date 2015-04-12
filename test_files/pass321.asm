  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        3
  3         STORE        3[LB]
  4         RETURN (0)   0
  5         LOAD         -1[LB]
  6         RETURN (1)   1
  7  L11:   LOADL        -1
  8         LOADL        2
  9         LOADA        1[CB]
 10         LOADA        5[CB]
 11         JUMP         L12
 12  L12:   LOADL        -1
 13         LOADL        0
 14         LOADL        -1
 15         CALL         L10
 16         HALT   (0)   
