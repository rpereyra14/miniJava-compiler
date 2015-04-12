  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        1
  3         STORE        3[LB]
  4         PUSH         1
  5         LOADL        5
  6         STORE        4[LB]
  7         LOAD         3[LB]
  8         LOAD         4[LB]
  9         CALL         add     
 10         STORE        3[LB]
 11         POP          1
 12         LOAD         3[LB]
 13         LOADL        1
 14         CALL         add     
 15         STORE        3[LB]
 16         PUSH         1
 17         LOADL        9
 18         STORE        4[LB]
 19         LOAD         3[LB]
 20         LOAD         4[LB]
 21         CALL         add     
 22         STORE        3[LB]
 23         POP          1
 24         LOAD         3[LB]
 25         CALL         putint  
 26         RETURN (0)   0
 27  L11:   LOADL        -1
 28         LOADL        1
 29         LOADA        1[CB]
 30         LOADL        -1
 31         CALL         L10
 32         HALT   (0)   
