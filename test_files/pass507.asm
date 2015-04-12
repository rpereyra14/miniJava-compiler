  0         JUMP         L12
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         LOADA        0[SB]
  5         LOADL        0
  6         CALL         newobj  
  7         STORE        3[LB]
  8         LOAD         3[LB]
  9         CALL         L11
 10         RETURN (0)   0
 11  L11:   LOADL        7
 12         CALL         putint  
 13         RETURN (0)   0
 14  L12:   LOADL        -1
 15         LOADL        2
 16         LOADA        1[CB]
 17         LOADA        11[CB]
 18         LOADL        -1
 19         CALL         L10
 20         HALT   (0)   
