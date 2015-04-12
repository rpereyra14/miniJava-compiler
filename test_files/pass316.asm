  0         JUMP         L14
  1  L10:   PUSH         1
  2         LOADA        0[SB]
  3         LOADL        0
  4         CALL         newobj  
  5         STORE        3[LB]
  6         PUSH         1
  7         LOAD         3[LB]
  8         CALL         L13
  9         LOAD         -1[ST]
 10         JUMPIF (0)   L11
 11         LOAD         3[LB]
 12         CALL         L12
 13         LOADL        5
 14         CALL         eq      
 15         CALL         and     
 16  L11:   STORE        4[LB]
 17         RETURN (0)   0
 18  L12:   LOADL        5
 19         RETURN (1)   0
 20  L13:   LOADL        1
 21         LOADL        0
 22         CALL         eq      
 23         RETURN (1)   0
 24  L14:   LOADL        -1
 25         LOADL        3
 26         LOADA        1[CB]
 27         LOADA        18[CB]
 28         LOADA        20[CB]
 29         LOADL        -1
 30         CALL         L10
 31         HALT   (0)   
