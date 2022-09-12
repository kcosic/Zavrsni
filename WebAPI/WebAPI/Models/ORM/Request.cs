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
    
    public partial class Request
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Request()
        {
            this.Issues = new HashSet<Issue>();
        }
    
        public int Id { get; set; }
        public System.DateTime DateCreated { get; set; }
        public System.DateTime DateModified { get; set; }
        public Nullable<System.DateTime> DateDeleted { get; set; }
        public bool Deleted { get; set; }
        public int UserId { get; set; }
        public int ShopId { get; set; }
        public decimal Price { get; set; }
        public System.DateTime EstimatedFinishDate { get; set; }
        public System.DateTime ActualFinishDate { get; set; }
        public decimal EstimatedPrice { get; set; }
        public decimal ActualPrice { get; set; }
        public string BillPicture { get; set; }
        public bool Completed { get; set; }
    
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<Issue> Issues { get; set; }
        public virtual Shop Shop { get; set; }
        public virtual User User { get; set; }
    }
}
