  0         JUMP         L14
  1  L10:   PUSH         1
  2         LOADA        0[SB]
  3         LOADL        0
  4         CALL         newobj  
  5         STORE        3[LB]
  6         PUSH         1
  7         LOADL        0
  8         STORE        4[LB]
  9         JUMP         L12
 10  L11:   LOAD         3[LB]
 11         CALL         L13
 12         POP          1
 13         LOAD         4[LB]
 14         LOADL        1
 15         CALL         add     
 16         STORE        4[LB]
 17  L12:   LOAD         4[LB]
 18         LOADL        1025
 19         CALL         lt      
 20         JUMPIF (1)   L11
 21         LOADL        25
 22         CALL         putint  
 23         RETURN (0)   0
 24  L13:   LOADL        55
 25         RETURN (1)   0
 26  L14:   LOADL        -1
 27         LOADL        2
 28         LOADA        1[CB]
 29         LOADA        24[CB]
 30         LOADL        -1
 31         CALL         L10
 32         HALT   (0)   
