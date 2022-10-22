using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;
using WebAPI.Models.Helpers;

namespace WebAPI.Models.ORM
{
    partial class Shop
    {
        public static ShopDTO ToDTO(Shop item, bool singleLevel = true)
        {
            if (item == null)
            {
                return null;
            }

            return new ShopDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Location = singleLevel ? null : Location.ToDTO(item.Location),
                Requests = singleLevel ? null : Request.ToListDTO(item.Requests),
                Reviews = singleLevel ? null : Review.ToListDTO(item.Reviews),
                Appointments = singleLevel ? null : Appointment.ToListDTO(item.Appointments),
                ChildShops = singleLevel ? null : Shop.ToListDTO(item.ChildShops),
                ParentShop = singleLevel ? null : Shop.ToDTO(item.ParentShop),
                ParentShopId = item.ParentShopId,
                Tokens = singleLevel ? null : Token.ToListDTO(item.Tokens),
                LegalName = item.LegalName,
                LocationId = item.LocationId,
                ShortName = item.ShortName,
                Vat = item.Vat,
                Email = item.Email,
                Password = Helper.PASSWORD_PLACEHOLDER,
                WorkDays = item.WorkDays,
                WorkHours = item.WorkHours,
                CarCapacity = item.CarCapacity
            };
        }

        public static ICollection<ShopDTO> ToListDTO(ICollection<Shop> list, bool singleLevel = true)
        {
            if (list == null)
            {
                return null;
            }
            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public ShopDTO ToDTO(bool singleLevel = true)
        {
            return new ShopDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Location = singleLevel ? null : Location.ToDTO(Location),
                Requests = singleLevel ? null : Request.ToListDTO(Requests),
                Reviews = singleLevel ? null : Review.ToListDTO(Reviews),
                Appointments = singleLevel ? null : Appointment.ToListDTO(Appointments),
                LegalName = LegalName,
                LocationId = LocationId,
                ShortName = ShortName,
                Vat = Vat,
                Email = Email,
                Password = Helper.PASSWORD_PLACEHOLDER,
                ChildShops = singleLevel ? null : Shop.ToListDTO(ChildShops),
                ParentShop = singleLevel ? null : Shop.ToDTO(ParentShop),
                ParentShopId = ParentShopId,
                Tokens = singleLevel ? null : Token.ToListDTO(Tokens),
                WorkDays = WorkDays,
                WorkHours = WorkHours,
                CarCapacity = CarCapacity
            };
        }
    }

}