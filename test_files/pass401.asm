  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        1
  3         STORE        3[LB]
  4         LOAD         3[LB]
  5         CALL         putint  
  6         RETURN (0)   0
  7  L11:   LOADL        -1
  8         LOADL        1
  9         LOADA        1[CB]
 10         LOADL        -1
 11         CALL         L10
 12         HALT   (0)   
