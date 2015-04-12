  0         JUMP         L12
  1  L10:   PUSH         1
  2         LOADA        0[SB]
  3         LOADL        1
  4         CALL         newobj  
  5         STORE        3[LB]
  6         PUSH         1
  7         LOAD         3[LB]
  8         CALL         L11
  9         LOAD         3[LB]
 10         LOADL        0
 11         CALL         fieldref
 12         CALL         add     
 13         STORE        4[LB]
 14         RETURN (0)   0
 15  L11:   LOADL        3
 16         RETURN (1)   0
 17  L12:   LOADL        -1
 18         LOADL        2
 19         LOADA        1[CB]
 20         LOADA        15[CB]
 21         LOADL        -1
 22         CALL         L10
 23         HALT   (0)   
