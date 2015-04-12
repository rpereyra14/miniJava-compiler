  0         JUMP         L13
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         LOADL        3
  5         STORE        3[LB]
  6         PUSH         1
  7         LOADL        0
  8         STORE        4[LB]
  9         JUMP         L12
 10  L11:   LOAD         4[LB]
 11         LOADL        1
 12         CALL         add     
 13         STORE        4[LB]
 14         LOAD         4[LB]
 15         STORE        3[LB]
 16  L12:   LOAD         4[LB]
 17         LOADL        4
 18         CALL         lt      
 19         JUMPIF (1)   L11
 20         LOAD         3[LB]
 21         CALL         putint  
 22         RETURN (0)   0
 23  L13:   LOADL        -1
 24         LOADL        1
 25         LOADA        1[CB]
 26         LOADL        -1
 27         CALL         L10
 28         HALT   (0)   
