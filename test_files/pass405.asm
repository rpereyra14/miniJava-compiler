  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        1
  4         CALL         newobj  
  5         STORE        3[LB]
  6         PUSH         1
  7         LOADL        5
  8         LOAD         3[LB]
  9         LOADL        0
 10         CALL         fieldref
 11         CALL         add     
 12         STORE        4[LB]
 13         LOAD         4[LB]
 14         CALL         putint  
 15         RETURN (0)   0
 16  L11:   LOADL        -1
 17         LOADL        1
 18         LOADA        1[CB]
 19         JUMP         L12
 20  L12:   LOADL        -1
 21         LOADL        0
 22         LOADL        -1
 23         CALL         L10
 24         HALT   (0)   
