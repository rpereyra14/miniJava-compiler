  0         JUMP         L15
  1  L10:   PUSH         1
  2         LOADA        0[SB]
  3         LOADL        0
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOADL        1
  7         LOADL        7
  8         LOAD         3[LB]
  9         CALL         L11
 10         CALL         add     
 11         CALL         putint  
 12         RETURN (0)   0
 13  L11:   PUSH         1
 14         LOADL        1
 15         CALL         neg     
 16         STORE        4[LB]
 17         LOAD         -1[LB]
 18         LOADL        0
 19         CALL         le      
 20         JUMPIF (0)   L12
 21         LOADL        0
 22         STORE        4[LB]
 23         JUMP         L14
 24  L12:   LOAD         -1[LB]
 25         LOADL        1
 26         CALL         eq      
 27         JUMPIF (0)   L13
 28         LOADL        1
 29         STORE        4[LB]
 30         JUMP         L14
 31  L13:   LOAD         -1[LB]
 32         LOADL        1
 33         CALL         sub     
 34         LOADA        0[OB]
 35         CALL         L11
 36         LOAD         -1[LB]
 37         LOADL        2
 38         CALL         sub     
 39         LOADA        0[OB]
 40         CALL         L11
 41         CALL         add     
 42         STORE        4[LB]
 43  L14:   LOAD         4[LB]
 44         RETURN (1)   1
 45  L15:   LOADL        -1
 46         LOADL        2
 47         LOADA        1[CB]
 48         LOADA        13[CB]
 49         LOADL        -1
 50         CALL         L10
 51         HALT   (0)   
