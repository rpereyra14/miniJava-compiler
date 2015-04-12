  0         JUMP         L15
  1  L10:   PUSH         1
  2         LOADA        0[SB]
  3         LOADL        1
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        0
  8         LOADL        0
  9         CALL         fieldupd
 10         PUSH         1
 11         LOADL        22
 12         STORE        4[LB]
 13         PUSH         1
 14         LOADL        0
 15         LOAD         -1[ST]
 16         JUMPIF (0)   L11
 17         LOAD         3[LB]
 18         CALL         L14
 19         CALL         and     
 20  L11:   STORE        5[LB]
 21         PUSH         1
 22         LOADL        1
 23         LOAD         -1[ST]
 24         JUMPIF (1)   L12
 25         LOAD         3[LB]
 26         CALL         L14
 27         CALL         or      
 28  L12:   CALL         not     
 29         STORE        6[LB]
 30         LOAD         3[LB]
 31         LOADL        0
 32         CALL         fieldref
 33         JUMPIF (0)   L13
 34         LOADL        1
 35         CALL         neg     
 36         STORE        4[LB]
 37  L13:   LOAD         4[LB]
 38         CALL         putint  
 39         RETURN (0)   0
 40  L14:   LOADA        0[OB]
 41         LOADL        0
 42         LOADL        1
 43         CALL         fieldupd
 44         LOADL        1
 45         RETURN (1)   0
 46  L15:   LOADL        -1
 47         LOADL        2
 48         LOADA        1[CB]
 49         LOADA        40[CB]
 50         LOADL        -1
 51         CALL         L10
 52         HALT   (0)   
