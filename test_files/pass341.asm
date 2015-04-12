  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         LOADL        1
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         CALL         putint  
  8         RETURN (0)   0
  9  L11:   LOADL        -1
 10         LOADL        1
 11         LOADA        1[CB]
 12         LOADL        -1
 13         CALL         L10
 14         HALT   (0)   
