  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        2
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOADL        10
  7         LOAD         3[LB]
  8         CALL         L12
  9         RETURN (0)   0
 10  L11:   LOADL        -1
 11         LOADL        1
 12         LOADA        1[CB]
 13         JUMP         L25
 14  L12:   LOAD         -1[LB]
 15         LOADA        0[OB]
 16         CALL         L21
 17         LOADA        0[OB]
 18         CALL         L18
 19         LOADL        99999
 20         CALL         putint  
 21         LOADA        0[OB]
 22         CALL         L13
 23         LOADA        0[OB]
 24         CALL         L18
 25         LOADL        99999
 26         CALL         putint  
 27         RETURN (0)   1
 28  L13:   PUSH         1
 29         LOADA        0[OB]
 30         LOADL        1
 31         CALL         fieldref
 32         LOADL        1
 33         CALL         sub     
 34         STORE        4[LB]
 35         PUSH         1
 36         LOADL        0
 37         STORE        5[LB]
 38         JUMP         L16
 39  L14:   LOADA        0[OB]
 40         LOADL        0
 41         CALL         add     
 42         LOAD         5[LB]
 43         CALL         arrayref
 44         LOADA        0[OB]
 45         LOADL        0
 46         CALL         add     
 47         LOAD         5[LB]
 48         LOADL        1
 49         CALL         add     
 50         CALL         arrayref
 51         CALL         le      
 52         JUMPIF (0)   L15
 53         LOAD         5[LB]
 54         LOADL        1
 55         CALL         add     
 56         STORE        5[LB]
 57         JUMP         L16
 58  L15:   LOADA        0[OB]
 59         LOADL        0
 60         CALL         fieldref
 61         LOAD         5[LB]
 62         LOAD         5[LB]
 63         LOADL        1
 64         CALL         add     
 65         LOADA        0[OB]
 66         CALL         L17
 67         LOAD         5[LB]
 68         LOADL        0
 69         CALL         gt      
 70         JUMPIF (0)   L16
 71         LOAD         5[LB]
 72         LOADL        1
 73         CALL         sub     
 74         STORE        5[LB]
 75  L16:   LOAD         5[LB]
 76         LOAD         4[LB]
 77         CALL         lt      
 78         JUMPIF (1)   L14
 79         RETURN (0)   0
 80  L17:   PUSH         1
 81         LOAD         -1[LB]
 82         LOAD         -2[LB]
 83         CALL         arrayref
 84         STORE        6[LB]
 85         LOAD         -1[LB]
 86         LOAD         -2[LB]
 87         LOAD         -1[LB]
 88         LOAD         -3[LB]
 89         CALL         arrayref
 90         CALL         arrayupd
 91         LOAD         -1[LB]
 92         LOAD         -3[LB]
 93         LOAD         6[LB]
 94         CALL         arrayupd
 95         RETURN (0)   3
 96  L18:   PUSH         1
 97         LOADL        0
 98         STORE        7[LB]
 99         JUMP         L20
100  L19:   LOADA        0[OB]
101         LOADL        0
102         CALL         add     
103         LOAD         7[LB]
104         CALL         arrayref
105         CALL         putint  
106         LOAD         7[LB]
107         LOADL        1
108         CALL         add     
109         STORE        7[LB]
110  L20:   LOAD         7[LB]
111         LOADA        0[OB]
112         LOADL        1
113         CALL         fieldref
114         CALL         lt      
115         JUMPIF (1)   L19
116         RETURN (0)   0
117  L21:   LOADA        0[OB]
118         LOADL        0
119         LOAD         -1[LB]
120         CALL         newarr  
121         CALL         fieldupd
122         LOADA        0[OB]
123         LOADL        1
124         LOADA        0[OB]
125         LOADL        0
126         LOAD         -1[OB]
127         CALL         fieldupd
128         PUSH         1
129         LOADL        17
130         STORE        8[LB]
131         PUSH         1
132         LOADL        1
133         STORE        9[LB]
134         JUMP         L23
135  L22:   LOADA        0[OB]
136         LOADL        0
137         CALL         add     
138         LOAD         9[LB]
139         LOAD         8[LB]
140         CALL         mult    
141         LOADA        0[OB]
142         LOADL        1
143         CALL         fieldref
144         LOADA        0[OB]
145         CALL         L24
146         LOAD         9[LB]
147         CALL         arrayupd
148         LOAD         9[LB]
149         LOADL        1
150         CALL         add     
151         STORE        9[LB]
152  L23:   LOAD         9[LB]
153         LOADA        0[OB]
154         LOADL        1
155         CALL         fieldref
156         CALL         le      
157         JUMPIF (1)   L22
158         RETURN (0)   1
159  L24:   LOAD         -1[LB]
160         LOAD         -1[LB]
161         LOAD         -2[LB]
162         CALL         div     
163         LOAD         -2[LB]
164         CALL         mult    
165         CALL         sub     
166         RETURN (1)   2
167  L25:   LOADL        -1
168         LOADL        6
169         LOADA        14[CB]
170         LOADA        28[CB]
171         LOADA        80[CB]
172         LOADA        96[CB]
173         LOADA        117[CB]
174         LOADA        159[CB]
175         LOADL        -1
176         CALL         L10
177         HALT   (0)   
