  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        2
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        1
  8         LOADA        6[SB]
  9         LOADL        2
 10         CALL         newobj  
 11         CALL         fieldupd
 12         LOAD         3[LB]
 13         LOADL        1
 14         CALL         fieldref
 15         LOADL        1
 16         LOAD         3[LB]
 17         CALL         fieldupd
 18         LOAD         3[LB]
 19         CALL         L12
 20         RETURN (0)   0
 21  L11:   LOADL        -1
 22         LOADL        1
 23         LOADA        1[CB]
 24         JUMP         L13
 25  L12:   PUSH         1
 26         LOADL        10
 27         STORE        4[LB]
 28         LOADL        8
 29         LOADL        3
 30         LOADA        0[OB]
 31         LOADL        1
 32         CALL         fieldref
 33         CALL         L14
 34         CALL         add     
 35         CALL         putint  
 36         RETURN (0)   0
 37  L13:   LOADL        -1
 38         LOADL        1
 39         LOADA        25[CB]
 40         JUMP         L16
 41  L14:   PUSH         1
 42         LOADL        1
 43         STORE        5[LB]
 44         LOAD         -1[LB]
 45         LOADL        1
 46         CALL         gt      
 47         JUMPIF (0)   L15
 48         LOAD         -1[LB]
 49         LOAD         -1[LB]
 50         LOADL        1
 51         CALL         sub     
 52         LOADA        0[OB]
 53         CALL         L14
 54         CALL         mult    
 55         STORE        5[LB]
 56  L15:   LOAD         5[LB]
 57         RETURN (1)   1
 58  L16:   LOADL        -1
 59         LOADL        1
 60         LOADA        41[CB]
 61         LOADL        -1
 62         CALL         L10
 63         HALT   (0)   
