//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace WebAPI.Models.ORM
{
    using System;
    using System.Collections.Generic;
    
    public partial class Log
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public System.DateTime Timestamp { get; set; }
        public string Application { get; set; }
        public string Source { get; set; }
        public string Severity { get; set; }
        public string Message { get; set; }
    }
}