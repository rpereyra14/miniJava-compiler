  0         JUMP         L12
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         LOAD         3[LB]
  5         CALL         L11
  6         RETURN (0)   0
  7  L11:   LOADL        4
  8         CALL         putint  
  9         RETURN (0)   0
 10  L12:   LOADL        -1
 11         LOADL        2
 12         LOADA        1[CB]
 13         LOADA        7[CB]
 14         LOADL        -1
 15         CALL         L10
 16         HALT   (0)   
