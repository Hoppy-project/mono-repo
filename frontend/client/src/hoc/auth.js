// import React, { useEffect } from 'react';

// export default function (SpecificComponent) {
//     function AuthenticationCheck(props) {

//         const token = localStorage.getItem('Authorization');

//         useEffect(() => {
//             let isCleaned = true
//             if (token === null) {
//                 if (isCleaned) {
//                     props.history.push('/login')
//                     return;
//                 }
//             }
//             else if (token !== null) {
//                 props.history.push('/')    
//             }
//             return () => {
//                 isCleaned = false
//             }
//         }, [])

//         return (
//             <SpecificComponent {...props} />
//           )
//     }
//     return AuthenticationCheck
// }