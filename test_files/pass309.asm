  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        2
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        0
  8         CALL         fieldref
  9         LOADL        0
 10         CALL         fieldref
 11         LOADL        1
 12         LOADL        3
 13         CALL         fieldupd
 14         RETURN (0)   0
 15  L11:   LOADL        -1
 16         LOADL        1
 17         LOADA        1[CB]
 18         JUMP         L12
 19  L12:   LOADL        -1
 20         LOADL        0
 21         LOADL        -1
 22         CALL         L10
 23         HALT   (0)   
