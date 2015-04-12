  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         PUSH         1
  5         LOADA        3[SB]
  6         LOADL        1
  7         CALL         newobj  
  8         STORE        4[LB]
  9         LOADL        5
 10         LOAD         4[LB]
 11         LOADL        0
 12         CALL         fieldref
 13         CALL         add     
 14         STORE        3[LB]
 15         LOAD         3[LB]
 16         CALL         putint  
 17         RETURN (0)   0
 18  L11:   LOADL        -1
 19         LOADL        1
 20         LOADA        1[CB]
 21         JUMP         L12
 22  L12:   LOADL        -1
 23         LOADL        0
 24         LOADL        -1
 25         CALL         L10
 26         HALT   (0)   
