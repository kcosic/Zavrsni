using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebAPI.Models
{
    public class PasswordReset
    {
        public string OldPassword { get; set; }
        public string NewPassword { get; set; }
    }
}