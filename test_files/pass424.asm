  0         JUMP         L13
  1  L10:   PUSH         1
  2         LOADL        24
  3         STORE        3[LB]
  4         PUSH         1
  5         LOADL        0
  6         STORE        4[LB]
  7         JUMP         L12
  8  L11:   PUSH         1
  9         LOAD         4[LB]
 10         STORE        5[LB]
 11         LOAD         4[LB]
 12         LOADL        1
 13         CALL         add     
 14         STORE        4[LB]
 15         POP          1
 16  L12:   LOAD         4[LB]
 17         LOADL        1025
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
