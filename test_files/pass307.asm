  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        0[SB]
  3         LOADL        2
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        0
  8         LOAD         3[LB]
  9         CALL         fieldupd
 10         LOAD         3[LB]
 11         LOADL        0
 12         CALL         fieldref
 13         LOADL        0
 14         CALL         fieldref
 15         LOADL        1
 16         LOADL        3
 17         CALL         fieldupd
 18         RETURN (0)   0
 19  L11:   LOADL        -1
 20         LOADL        1
 21         LOADA        1[CB]
 22         LOADL        -1
 23         CALL         L10
 24         HALT   (0)   
