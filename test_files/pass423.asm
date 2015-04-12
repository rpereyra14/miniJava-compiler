  0         JUMP         L25
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
 11         LOADL        23
 12         STORE        4[LB]
 13         PUSH         1
 14         LOADL        1
 15         STORE        5[LB]
 16         PUSH         1
 17         LOADL        0
 18         STORE        6[LB]
 19         LOAD         5[LB]
 20         LOAD         -1[ST]
 21         JUMPIF (1)   L11
 22         LOAD         5[LB]
 23         LOAD         3[LB]
 24         CALL         L24
 25         CALL         or      
 26  L11:   LOAD         -1[ST]
 27         JUMPIF (0)   L12
 28         LOAD         6[LB]
 29         CALL         and     
 30  L12:   LOAD         -1[ST]
 31         JUMPIF (0)   L13
 32         LOAD         5[LB]
 33         LOAD         3[LB]
 34         CALL         L24
 35         CALL         and     
 36  L13:   JUMPIF (0)   L14
 37         LOADL        1
 38         CALL         neg     
 39         STORE        4[LB]
 40  L14:   LOAD         5[LB]
 41         LOAD         -1[ST]
 42         JUMPIF (0)   L15
 43         LOAD         5[LB]
 44         CALL         and     
 45  L15:   LOAD         -1[ST]
 46         JUMPIF (0)   L16
 47         LOAD         6[LB]
 48         CALL         and     
 49  L16:   LOAD         -1[ST]
 50         JUMPIF (0)   L17
 51         LOAD         5[LB]
 52         LOAD         3[LB]
 53         CALL         L24
 54         CALL         and     
 55  L17:   LOAD         -1[ST]
 56         JUMPIF (1)   L18
 57         LOAD         6[LB]
 58         CALL         or      
 59  L18:   LOAD         -1[ST]
 60         JUMPIF (1)   L19
 61         LOAD         6[LB]
 62         CALL         or      
 63  L19:   LOAD         -1[ST]
 64         JUMPIF (1)   L20
 65         LOAD         5[LB]
 66         CALL         or      
 67  L20:   LOAD         -1[ST]
 68         JUMPIF (1)   L21
 69         LOAD         5[LB]
 70         LOAD         3[LB]
 71         CALL         L24
 72         CALL         or      
 73  L21:   CALL         not     
 74         JUMPIF (0)   L22
 75         LOADL        1
 76         CALL         neg     
 77         STORE        4[LB]
 78  L22:   LOAD         3[LB]
 79         LOADL        0
 80         CALL         fieldref
 81         JUMPIF (0)   L23
 82         LOADL        1
 83         CALL         neg     
 84         STORE        4[LB]
 85  L23:   LOAD         4[LB]
 86         CALL         putint  
 87         RETURN (0)   0
 88  L24:   LOADA        0[OB]
 89         LOADL        0
 90         LOADL        1
 91         CALL         fieldupd
 92         LOAD         -1[LB]
 93         RETURN (1)   1
 94  L25:   LOADL        -1
 95         LOADL        2
 96         LOADA        1[CB]
 97         LOADA        88[CB]
 98         LOADL        -1
 99         CALL         L10
100         HALT   (0)   
