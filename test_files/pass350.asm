  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        1
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         CALL         L12
  8         RETURN (0)   0
  9  L11:   LOADL        -1
 10         LOADL        1
 11         LOADA        1[CB]
 12         JUMP         L13
 13  L12:   PUSH         1
 14         LOADL        10
 15         STORE        4[LB]
 16         LOAD         4[LB]
 17         CALL         putint  
 18         RETURN (0)   0
 19  L13:   LOADL        -1
 20         LOADL        1
 21         LOADA        13[CB]
 22         LOADL        -1
 23         CALL         L10
 24         HALT   (0)   
