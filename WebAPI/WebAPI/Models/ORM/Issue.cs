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
    
    public partial class Issue
    {
        public int Id { get; set; }
        public System.DateTime DateCreated { get; set; }
        public System.DateTime DateModified { get; set; }
        public Nullable<System.DateTime> DateDeleted { get; set; }
        public bool Deleted { get; set; }
        public int RequestId { get; set; }
        public string Description { get; set; }
        public decimal Price { get; set; }
        public Nullable<bool> Accepted { get; set; }
    
        public virtual Request Request { get; set; }
    }
}