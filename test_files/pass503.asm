  0         JUMP         L15
  1  L10:   PUSH         1
  2         LOADA        0[SB]
  3         LOADL        0
  4         CALL         newobj  
  5         STORE        3[LB]
  6         PUSH         1
  7         LOADL        0
  8         STORE        4[LB]
  9         LOADL        6
 10         STORE        4[LB]
 11         JUMP         L12
 12  L11:   LOAD         4[LB]
 13         LOAD         3[LB]
 14         CALL         L14
 15         POP          1
 16         LOAD         4[LB]
 17         LOADL        1
 18         CALL         add     
 19         STORE        4[LB]
 20  L12:   LOAD         4[LB]
 21         LOADL        9
 22         CALL         lt      
 23         JUMPIF (1)   L11
 24         LOAD         4[LB]
 25         LOADL        0
 26         CALL         ne      
 27         JUMPIF (0)   L13
 28         LOADL        3
 29         CALL         putint  
 30  L13:   RETURN (0)   0
 31  L14:   LOAD         -1[LB]
 32         RETURN (1)   1
 33  L15:   LOADL        -1
 34         LOADL        2
 35         LOADA        1[CB]
 36         LOADA        31[CB]
 37         LOADL        -1
 38         CALL         L10
 39         HALT   (0)   
